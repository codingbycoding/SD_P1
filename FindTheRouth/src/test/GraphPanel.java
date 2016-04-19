/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author onezero
 */
public class GraphPanel extends JPanel {

    public GraphPanel() {
        arrCircleIndex = new ArrayList<CircleIndex>();
        arrCircleLines = new ArrayList<CircleLine>();
        arrCircleDistrubLines = new ArrayList<CircleLine>();
        arrRouteLines = new ArrayList<CircleLine>();
        arrPickupCircleIndex = new ArrayList<CircleIndex>();
        arrColorCircleIndex = new ArrayList<CircleIndex>();
        lineMap = new HashMap<CircleIndex, ArrayList<CircleIndex>>();
        genCircleIndex();
        genCircleLines();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int i = 0; i < arrCircleIndex.size(); i++) {
            arrCircleIndex.get(i).draw(g);
        }

        for (int i = 0; i < arrCircleLines.size(); i++) {
            arrCircleLines.get(i).draw(g);
        }

        for (int i = 0; i < arrCircleDistrubLines.size(); i++) {
           arrCircleDistrubLines.get(i).draw(g);
        }
                
        for (int i = 0; i < arrRouteLines.size(); i++) {
            arrRouteLines.get(i).draw(g);
        }


//        for (int i = 0; i < arrColorCircleIndex.size(); i++) {
//            arrColorCircleIndex.get(i).draw(g);
//        }
//
//        for (int i = 0; i < arrPickupCircleIndex.size(); i++) {
//            arrPickupCircleIndex.get(i).draw(g);
//        }
    }

    public void genCircleIndex() {
        int circleNum = TestUtils.get5To8();
        ArrayList<Integer> arrNumEachRow = TestUtils.randomWithWeight(circleNum);
        ArrayList<Integer> arrRows = TestUtils.shuffle0To9();

        for (int i = 0; i < arrNumEachRow.size(); i++) {
            ArrayList<Integer> arrRowEach = TestUtils.shuffle0To9();
            for (int k = arrNumEachRow.get(i), j = 0; k > 0; k--, j++) {
                CircleIndex index = new CircleIndex(arrRows.get(i), arrRowEach.get(j), Color.BLACK);
                arrCircleIndex.add(index);
            }
        }

    }

    public void genCircleLines() {
        if (arrCircleIndex.size() > 1) {
            for (int i = 0; i < arrCircleIndex.size(); i++) {
                if (i != arrCircleIndex.size() - 1) {
                    arrCircleLines.add(new CircleLine(arrCircleIndex.get(i), arrCircleIndex.get(i+1), Color.BLACK));
                    //arrCircleLines.add(new CircleLine(arrCircleIndex.get(i+1), arrCircleIndex.get(i)));
                    ArrayList<CircleIndex> arr2CircleX, arr2CircleY = null;
                    arr2CircleX = lineMap.get(arrCircleIndex.get(i));
                    arr2CircleY = lineMap.get(arrCircleIndex.get(i + 1));

                    if (null == arr2CircleX) {
                        arr2CircleX = new ArrayList<CircleIndex>();
                    }

                    if (null == arr2CircleY) {
                        arr2CircleY = new ArrayList<CircleIndex>();
                    }

                    arr2CircleX.add(arrCircleIndex.get(i + 1));
                    lineMap.put(arrCircleIndex.get(i), arr2CircleX);

                    arr2CircleY.add(arrCircleIndex.get(i));
                    //lineMap.put(arrCircleIndex.get(i + 1), arr2CircleY);

                }
            }
        }
        

    arrCircleDistrubLines.add(new CircleLine(arrCircleIndex.get(1),arrCircleIndex.get(arrCircleIndex.size()-1), Color.BLACK));
           
    }

    public void refreshCircleIndex() {
        arrCircleIndex.clear();
        arrColorCircleIndex.clear();
        arrPickupCircleIndex.clear();
        arrCircleLines.clear();
        arrRouteLines.clear();
        arrCircleDistrubLines.clear();
        lineMap.clear();
        genCircleIndex();
        genCircleLines();
        repaint();
    }

    public void mouseOnCircleCheck(int x, int y) {
        boolean bRepaint = false;
        for (int i = 0; i < arrCircleIndex.size(); i++) {
            double dx = ((double) (4 * f_r + arrCircleIndex.get(i).x * 10 * f_r) - (double) x);
            double dy = ((double) (4 * f_r + arrCircleIndex.get(i).y * 10 * f_r) - (double) y);
            if (dx * dx + dy * dy <= (f_r + 6) * (f_r + 6)) {
                //change color
                arrCircleIndex.get(i).setColor(Color.ORANGE);
                arrColorCircleIndex.add(arrCircleIndex.get(i));
                bRepaint = true;
                break;
            }
        }

        if (!bRepaint && arrColorCircleIndex.size() > 0) {
            for(int i=0; i<arrColorCircleIndex.size(); i++) {
                arrColorCircleIndex.get(i).setColor(Color.BLACK);
            }
            
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
            double dx = ((double) (4 * f_r + arrColorCircleIndex.get(0).x * 10 * f_r) - (double) x);
            double dy = ((double) (4 * f_r + arrColorCircleIndex.get(0).y * 10 * f_r) - (double) y);
            if (dx * dx + dy * dy <= (f_r + 6) * (f_r + 6)) {
                //arrPickupCircleIndex.add(new CircleIndex(arrColorCircleIndex.get(0)));
                if (arrPickupCircleIndex.size() < 2) {
                    arrColorCircleIndex.get(0).setColor(Color.ORANGE);                    
                    CircleIndex circleIndex = arrColorCircleIndex.get(0);                    
                    arrPickupCircleIndex.add(circleIndex);     
                    arrColorCircleIndex.clear();
                    bRepaint = true;
                }
            }
        }

        if (bRepaint) {
            //logic for find the route.
            if (2 == arrPickupCircleIndex.size()) {
                CircleIndex circleBeg = arrPickupCircleIndex.get(0), circleEnd = arrPickupCircleIndex.get(1);
                for(int i=0; i<arrCircleIndex.size(); i++) {
                    if(circleEnd.equals(arrCircleIndex.get(i))) {
                        circleBeg = arrPickupCircleIndex.get(1);
                        circleEnd = arrPickupCircleIndex.get(0);
                        break;
                    } else if(circleBeg.equals(arrCircleIndex.get(i))) {
                        break;
                    }
                }
                         
                findRoute(circleBeg, circleEnd, null);
            }

            repaint();
        }
    }

    public void findRoute(CircleIndex beg, CircleIndex end, CircleIndex preCircle) {
        //static int limit_count = 0;
        if (beg == end) {
            return;
        }
        ArrayList<CircleIndex> arr2Circle = lineMap.get(beg);
        for (int i = 0; i < arr2Circle.size(); i++) {
            if (null == preCircle || preCircle != arr2Circle.get(i)) {
                arrRouteLines.add(new CircleLine(beg, arr2Circle.get(i), Color.RED));
                preCircle = beg;
                beg = arr2Circle.get(i);
                break;
            }
        }
           
        findRoute(beg, end, preCircle);

    }

    ArrayList<CircleIndex> arrCircleIndex;
    ArrayList<CircleIndex> arrColorCircleIndex;
    ArrayList<CircleIndex> arrPickupCircleIndex;
    ArrayList<CircleLine> arrCircleLines;
    ArrayList<CircleLine> arrCircleDistrubLines;
    ArrayList<CircleLine> arrRouteLines;
    Map<CircleIndex, ArrayList<CircleIndex>> lineMap;
    final int f_r = 6;
}

class CircleIndex {
    
    boolean equals(CircleIndex rhs) {
        boolean ret = false;
        if(rhs.x == this.x && rhs.y == this.y) {
            ret = true;
        }
        return ret;
    }

    CircleIndex(int i, int j, Color color) {
        this.x = i;
        this.y = j;
        this.color = color;
    }
    
    void setColor(Color color) {
        this.color = color;
    }
    
    int x;
    int y;
    final int f_r = 6;
    Color color;
    
    void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(4 * f_r + x * 10 * f_r - f_r, 4 * f_r + y * 10 * f_r - f_r, f_r * 4, f_r * 4);
        
    }
}

class CircleLine {

    CircleLine(CircleIndex beg, CircleIndex end, Color color) {
        this.beg = beg;
        this.end = end;
        this.color = color;
    }
    
    void setColor(Color color) {
        this.color = color;
    }
    
    void draw(Graphics g) {
        g.setColor(color);
        g.drawLine(4 * f_r + beg.x * 10 * f_r,
                4 * f_r + beg.y * 10 * f_r,
                4 * f_r + end.x * 10 * f_r,
                4 * f_r + end.y * 10 * f_r);

    }
    
    CircleIndex beg;
    CircleIndex end;
    final int f_r = 6;
    Color color;
}
