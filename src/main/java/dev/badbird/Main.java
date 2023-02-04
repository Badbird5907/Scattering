package dev.badbird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int width = 250, height = 250;
    private static final boolean saveImages = System.getProperty("saveImages", "false").equals("true");

    public static void main(String[] args) throws IOException {
        int times = Integer.parseInt(System.getProperty("times", "1000"));
        List<Info> infos = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            Info info = generate(i);
            infos.add(info);
        }
        int avgQ1 = 0;
        int avgQ2 = 0;
        int avgQ3 = 0;
        int avgQ4 = 0;

        for (Info info : infos) {
            avgQ1 += info.getQuadrant1();
            avgQ2 += info.getQuadrant2();
            avgQ3 += info.getQuadrant3();
            avgQ4 += info.getQuadrant4();
        }

        avgQ1 /= times;
        avgQ2 /= times;
        avgQ3 /= times;
        avgQ4 /= times;

        System.out.println("Average Q1: " + avgQ1);
        System.out.println("Average Q2: " + avgQ2);
        System.out.println("Average Q3: " + avgQ3);
        System.out.println("Average Q4: " + avgQ4);
    }

    public static Info generate(int i) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.black);
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, width, height);
        g2d.draw(circle);

        g2d.drawLine(0, height / 2, width, height / 2);
        g2d.drawLine(width / 2, 0, width / 2, height);

        for (Location location : getLocations(width, height, 100)) {
            g2d.setColor(Color.red);
            g2d.fillRect(location.getX(), location.getZ(), 1, 1);
        }

        // determine how many points are in each quadrant
        int quadrant1 = 0;
        int quadrant2 = 0;
        int quadrant3 = 0;
        int quadrant4 = 0;
        for (Location location : getLocations(width, height, 100)) {
            if (location.getX() > width / 2 && location.getZ() > height / 2) {
                quadrant1++;
            } else if (location.getX() < width / 2 && location.getZ() > height / 2) {
                quadrant2++;
            } else if (location.getX() < width / 2 && location.getZ() < height / 2) {
                quadrant3++;
            } else if (location.getX() > width / 2 && location.getZ() < height / 2) {
                quadrant4++;
            }
        }
        System.out.println("--------------------");
        System.out.println("Quadrant 1: " + quadrant1);
        System.out.println("Quadrant 2: " + quadrant2);
        System.out.println("Quadrant 3: " + quadrant3);
        System.out.println("Quadrant 4: " + quadrant4);
        System.out.println("--------------------");

        // write text to top lefr of each quadrant
        g2d.setColor(Color.black);
        g2d.drawString("Q1: " + quadrant1, 0, 10);
        g2d.drawString("Q2: " + quadrant2, width / 2, 10);
        g2d.drawString("Q3: " + quadrant3, 0, height / 2 + 10);
        g2d.drawString("Q4: " + quadrant4, width / 2, height / 2 + 10);

        File file = new File(i + ".png");
        try {
            if (saveImages)
                ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Info(quadrant1, quadrant2, quadrant3, quadrant4);
    }

    public static List<Location> getLocations(int x, int z, int times) { // x and z are radius
        List<Location> locations = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < times; i++) {
            int pX = random.nextInt(x);
            int pZ = random.nextInt(z);

            Location location = new Location(pX, pZ);

            locations.add(location);
        }
        return locations;
    }

    public static class Location {
        int x;
        int z;

        public Location(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setZ(int z) {
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }
    }

    public static class Info {
        int totalPoints;
        int quadrant1;
        int quadrant2;
        int quadrant3;
        int quadrant4;

        public Info(int quadrant1, int quadrant2, int quadrant3, int quadrant4) {
            this.quadrant1 = quadrant1;
            this.quadrant2 = quadrant2;
            this.quadrant3 = quadrant3;
            this.quadrant4 = quadrant4;
            this.totalPoints = quadrant1 + quadrant2 + quadrant3 + quadrant4;
        }

        public int getQuadrant1() {
            return quadrant1;
        }

        public int getQuadrant2() {
            return quadrant2;
        }

        public int getQuadrant3() {
            return quadrant3;
        }

        public int getQuadrant4() {
            return quadrant4;
        }

        public int getTotalPoints() {
            return totalPoints;
        }
    }
}
