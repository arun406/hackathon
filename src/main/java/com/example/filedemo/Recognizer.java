package com.example.filedemo;

import org.tensorflow.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Recognizer {

    private static byte[] graphDef;
    private static List<String> labels;

    public static void main(String[] args) {
        getBread("standard_poodle.jpeg");
    }

    public static String getBread(String imageName) {
        String modelpath = "/Users/mercator/hackathon/inception_dec_2015";
        byte[] imageBytes = readAllBytesOrExit(Paths.get("/Users/mercator/hackathon/images/" + imageName));

        try (Tensor image = Tensor.create(imageBytes)) {
            graphDef = readAllBytesOrExit(Paths.get(modelpath, "tensorflow_inception_graph.pb"));
            labels = readAllLinesOrExit(Paths.get(modelpath, "imagenet_comp_graph_label_strings.txt"));
            float[] labelProbabilities = executeInceptionGraph(graphDef, image);
            int bestLabelIdx = maxIndex(labelProbabilities);
            System.out.println(
                    String.format(
                            "BEST MATCH: %s (%.2f%% likely)",
                            labels.get(bestLabelIdx), labelProbabilities[bestLabelIdx] * 100f));
            return labels.get(bestLabelIdx);
        }
    }

    private static float[] executeInceptionGraph(byte[] graphDef, Tensor image) {
        try (Graph g = new Graph()) {
            g.importGraphDef(graphDef);
            try (Session s = new Session(g);
                 Tensor<float[]> result = (Tensor<float[]>) s.runner().feed("DecodeJpeg/contents", image).fetch("softmax").run().get(0)) {
                final long[] rshape = result.shape();
                if (result.numDimensions() != 2 || rshape[0] != 1) {
                    throw new RuntimeException(String.format(
                            "Expected model to produce a [1 N] shaped tensor where N is the number of labels, instead it produced one with shape %s",
                            Arrays.toString(rshape)));
                }
                int nlabels = (int) rshape[1];

                return (result.copyTo(new float[1][nlabels])[0]);
            }
        }
    }

    private static int maxIndex(float[] probabilities) {
        int best = 0;
        for (int i = 1; i < probabilities.length; ++i) {
            if (probabilities[i] > probabilities[best]) {
                best = i;
            }
        }
        return best;
    }

    private static byte[] readAllBytesOrExit(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private static List<String> readAllLinesOrExit(Path path) {
        try {
            return Files.readAllLines(path, Charset.forName("UTF-8"));
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    static class GraphBuilder {

        GraphBuilder(Graph g) {
            this.g = g;
        }

        Output div(Output x, Output y) {
            return binaryOp("Div", x, y);
        }

        Output sub(Output x, Output y) {
            return binaryOp("Sub", x, y);
        }

        Output resizeBilinear(Output images, Output size) {
            return binaryOp("ResizeBilinear", images, size);
        }

        Output expandDims(Output input, Output dim) {
            return binaryOp("ExpandDims", input, dim);
        }

        Output cast(Output value, DataType dtype) {
            return g.opBuilder("Cast", "Cast").addInput(value).setAttr("DstT", dtype).build().output(0);
        }

        Output decodeJpeg(Output contents, long channels) {
            return g.opBuilder("DecodeJpeg", "DecodeJpeg")
                    .addInput(contents)
                    .setAttr("channels", channels)
                    .build()
                    .output(0);
        }

        Output constant(String name, Object value) {
            try (Tensor t = Tensor.create(value)) {
                return g.opBuilder("Const", name)
                        .setAttr("dtype", t.dataType())
                        .setAttr("value", t)
                        .build()
                        .output(0);
            }
        }

        private Output binaryOp(String type, Output in1, Output in2) {
            return g.opBuilder(type, type).addInput(in1).addInput(in2).build().output(0);
        }

        private Graph g;
    }
}
