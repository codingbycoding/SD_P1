/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author onezero
 */
public class GraphPanel extends JPanel {

    public GraphPanel() {
        arrCircleIndex = new ArrayList<CircleIndex>();
        arrCircleLines = new ArrayList<CircleLine>();
        arrPickupCircleIndex = new ArrayList<CircleIndex>();
        arrColorCircleIndex = new ArrayList<CircleIndex>();
        genCircleIndex();
        genCircleLines();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //ArrayList arrList = new ArrayList();

        /*
        for (int i = 0; i < 10; i++) {
            //g.drawOval(2*f_r + i*f_r, 2*f_r + i*f_r, f_r*2, f_r*2);
            g.fillOval(4 * f_r + i * 10 * f_r - f_r, 4 * f_r + i * 10 * f_r - f_r, f_r * 4, f_r * 4);
        }
         */
        for (int i = 0; i < arrCircleIndex.size(); i++) {
            g.fillOval(4 * f_r + arrCircleIndex.get(i).i * 10 * f_r - f_r, 4 * f_r + arrCircleIndex.get(i).j * 10 * f_r - f_r, f_r * 4, f_r * 4);
        }

        for (int i = 0; i < arrCircleLines.size(); i++) {
            g.drawLine(
                    4 * f_r + arrCircleLines.get(i).beg.i * 10 * f_r,
                    4 * f_r + arrCircleLines.get(i).beg.j * 10 * f_r,
                    4 * f_r + arrCircleLines.get(i).end.i * 10 * f_r,
                    4 * f_r + arrCircleLines.get(i).end.j * 10 * f_r);
        }

        for (int i = 0; i < arrColorCircleIndex.size(); i++) {
            Color originalColor = g.getColor();
            g.setColor(Color.ORANGE);
            g.fillOval(4 * f_r + arrColorCircleIndex.get(i).i * 10 * f_r - f_r, 4 * f_r + arrColorCircleIndex.get(i).j * 10 * f_r - f_r, f_r * 4, f_r * 4);
            g.setColor(originalColor);
        }

        for (int i = 0; i < arrPickupCircleIndex.size(); i++) {
            Color originalColor = g.getColor();
            g.setColor(Color.ORANGE);
            g.fillOval(4 * f_r + arrPickupCircleIndex.get(i).i * 10 * f_r - f_r, 4 * f_r + arrPickupCircleIndex.get(i).j * 10 * f_r - f_r, f_r * 4, f_r * 4);
            g.setColor(originalColor);
        }
    }

    public void genCircleIndex() {
        int circleNum = TestUtils.get5To8();
        ArrayList<Integer> arrNumEachRow = TestUtils.randomWithWeight(circleNum);
        ArrayList<Integer> arrRows = TestUtils.shuffle0To9();

        for (int i = 0; i < arrNumEachRow.size(); i++) {
            ArrayList<Integer> arrRowEach = TestUtils.shuffle0To9();
            for (int k = arrNumEachRow.get(i), j = 0; k > 0; k--, j++) {
                CircleIndex index = new CircleIndex(arrRows.get(i), arrRowEach.get(j));
                arrCircleIndex.add(index);
            }
        }

    }

    public void genCircleLines() {
        if (arrCircleIndex.size() > 1) {
            for (int i = 0; i < arrCircleIndex.size(); i++) {
                if (i != arrCircleIndex.size() - 1) {
                    arrCircleLines.add(new CircleLine(arrCircleIndex.get(i), arrCircleIndex.get(i + 1)));
                }
            }
        }
    }

    public void refreshCircleIndex() {
        arrCircleIndex.clear();
        arrColorCircleIndex.clear();
        arrPickupCircleIndex.clear();
        arrCircleLines.clear();
        genCircleIndex();
        genCircleLines();
        repaint();
    }

    public void mouseOnCircleCheck(int x, int y) {
        boolean bRepaint = false;
        for (int i = 0; i < arrCircleIndex.size(); i++) {
            double dx = ((double) (4 * f_r + arrCircleIndex.get(i).i * 10 * f_r) - (double) x);
            double dy = ((double) (4 * f_r + arrCircleIndex.get(i).j * 10 * f_r) - (double) y);
            if (dx * dx + dy * dy <= (f_r + 6) * (f_r + 6)) {
                //change color
                arrColorCircleIndex.add(arrCircleIndex.get(i));
                bRepaint = true;
                break;
            }
        }

        if (!bRepaint && arrColorCircleIndex.size() > 0) {
            arrColorCircleIndex.clear();
            bRepaint = true;
        }
        if (bRepaint) {
            repaint();
        }

    }

    public void setPickupCircle(int x, int y) {
        boolean bRepaint = false;
        if (arrColorCircleIndex.size() > 0) {
            double dx = ((double) (4 * f_r + arrColorCircleIndex.get(0).i * 10 * f_r) - (double) x);
            double dy = ((double) (4 * f_r + arrColorCircleIndex.get(0).j * 10 * f_r) - (double) y);
            if (dx * dx + dy * dy <= (f_r + 6) * (f_r + 6)) {
                //arrPickupCircleIndex.add(new CircleIndex(arrColorCircleIndex.get(0)));
                if(arrPickupCircleIndex.size() < 2) {
                    arrPickupCircleIndex.add(arrColorCircleIndex.get(0));
                    arrColorCircleIndex.clear();
                    bRepaint = true;
                }                
            }
        }

        if (bRepaint) {
            //logic for find the route.
            repaint();
        }
    }
    
    public void findRoute() {
        
    }

    ArrayList<CircleIndex> arrCircleIndex;
    ArrayList<CircleIndex> arrColorCircleIndex;
    ArrayList<CircleIndex> arrPickupCircleIndex;
    ArrayList<CircleLine> arrCircleLines;
    final int f_r = 6;
}

class CircleIndex {
    CircleIndex(CircleIndex rhs) {
        this.i = rhs.i;
        this.j = rhs.j;
    }
    CircleIndex(int i, int j) {
        this.i = i;
        this.j = j;
    }
    int i;
    int j;
}

class CircleLine {

    CircleLine(CircleIndex beg, CircleIndex end) {
        this.beg = beg;
        this.end = end;
    }

    CircleIndex beg;
    CircleIndex end;
}
