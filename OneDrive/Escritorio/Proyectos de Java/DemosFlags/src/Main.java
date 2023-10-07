
import java.awt.*;

public class Main {

    public static final int COLOR_YELLOW = new Color(250,201,20).getRGB();
    public static final int COLOR_BLUE = new Color(17,13,99).getRGB();
    public static final int COLOR_RED = new Color(196,0,15).getRGB();
    public static final int COLOR_BLACK = new Color(5,5,5).getRGB();
    public static final int COLOR_WHITE = new Color(255,255,255).getRGB();
    public static final int COLOR_GREEN = new Color(35,138,51).getRGB();


    
    public static int[][] createCOLFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];

        int rowIn = 0;
        int rowend =0;

        rowend = (int)(heigth*0.50);
        for (int row = rowIn; row<rowend; row++) {
            for (int cell =0; cell<flag[row].length; cell ++) {
                flag[row][cell] = COLOR_YELLOW;
            }
        }
        rowend = (int)(heigth*0.75);
        for (int row = (int)(heigth*0.5); row<rowend; row++) {
            for (int cell =0; cell<flag[row].length; cell ++) {
                flag[row][cell] = COLOR_BLUE;
            }
        }
        rowend = (int)(heigth*1);
        for (int row = (int)(heigth*0.75); row<rowend; row++) {
            for (int cell =0; cell<flag[row].length; cell ++) {
                flag[row][cell] = COLOR_RED;
            }
        }

    return flag;
    }
    public static int[][] createVENFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;

        rowend = (int)(heigth*0.4);
        for (int row = 0; row<rowend; row++) {
            for (int cell =0; cell<flag[row].length; cell ++) {
                flag[row][cell] = COLOR_YELLOW;
            }
        }
        rowend = (int)(heigth*0.7);
        for (int row = (int)(heigth*0.4); row<rowend; row++) {
            for (int cell =0; cell<flag[row].length; cell ++) {
                flag[row][cell] = COLOR_BLUE;
            }
        }
        rowend = (int)(heigth*1);
        for (int row = (int)(heigth*0.7); row<rowend; row++) {
            for (int cell =0; cell<flag[row].length; cell ++) {
                flag[row][cell] = COLOR_RED;
            }
        }

        return flag;
    }
    public static int[][] createPOLFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;

        rowend = (int)(heigth*0.5);
        for (int row = 0; row<rowend; row++) {
            for (int cell =0; cell<flag[row].length; cell ++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        rowend = (int)(heigth*1);
        for (int row = (int)(heigth*0.5); row<rowend; row++) {
            for (int cell = 0; cell < flag[row].length; cell++) {
                flag[row][cell] = COLOR_RED;
            }
        }

        return flag;
    }
    public static int[][] createPANFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;
        int cellend = 0;

        cellend = (int)(width*0.5);
        rowend = (int)(heigth*0.5);
        for (int row = 0; row<rowend; row++) {
            for (int cell =0; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }

        cellend = (int)(width);
        rowend = (int)(heigth*0.5);
        for (int row = 0; row<rowend; row++) {
            for (int cell = (int)(width*0.5); cell<cellend; cell ++) {
                flag[row][cell] = COLOR_RED;
            }
        }
        cellend = (int)(width*0.5);
        rowend = (int)(heigth);
        for (int row = (int)(heigth*0.5); row<rowend; row++) {
            for (int cell = 0; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_BLUE;
            }
        }
        cellend = (int)(width);
        rowend = (int)(heigth);
        for (int row = (int)(heigth*0.5); row<rowend; row++) {
            for (int cell = (int)(width*0.5); cell<cellend; cell ++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        return flag;
    }
    public static int[][] createCHLFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;
        int cellend = 0;

        rowend = (int)(heigth*0.50);
        cellend = (int)(width*0.3);
        for (int row = 0; row<rowend; row++) {
            for (int cell =0; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_BLUE;
            }
        }
        rowend = (int)(heigth*0.5);
        cellend = (int)(width);
        for (int row = 0; row<rowend; row++) {
            for (int cell =(int)(width*0.3); cell<cellend; cell ++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        rowend = (int)(heigth);
        cellend = (int)(width);
        for (int row = (int)(heigth*0.5); row<rowend; row++) {
            for (int cell =0; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_RED;
            }
        }
        return flag;
    }
    public static int[][] createUSAFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;
        int cellend = 0;

        rowend = (int)(heigth*0.50);
        cellend = (int)(width*0.35);
        for (int row = 0; row<rowend; row++) {
            for (int cell =0; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_BLUE;
            }
        }

        rowend = (int)(heigth*0.50);
        cellend = (int)(width);
        for (int row = 0; row<rowend; row++) {
            for (int cell = (int)(width*0.35); cell<cellend; cell ++) {

                if (row%2 == 0) {
                    flag[row][cell] = COLOR_RED;
                else {
                    flag[row][cell] = COLOR_WHITE;
                }

            }
        }
        rowend = (int)(heigth);
        cellend = (int)(width);
        for (int row = (int)(heigth*0.5); row<rowend; row++) {
            for (int cell = 0; cell<cellend; cell ++) {

                if (row%2 == 0) {
                    flag[row][cell] = COLOR_RED;
                }
                else {
                    flag[row][cell] = COLOR_WHITE;
                }

            }
        }
        return flag;
    }
    public static int[][] createCZEFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;
        int cellend = 0;
        int x = 1;
        rowend = (int)(heigth*0.5);
        cellend = (int)(width) ;
        
        for (int row = 0; row<rowend; row++) {
            for (int cell =0; cell<x; cell ++) {
                flag[row][cell] = COLOR_BLUE;

            }
            x++;
        }
        
        x = 6;
        rowend = (int)(heigth);
        for (int row = (int)(heigth*0.5); row<rowend; row++) {
            for (int cell =x-1; cell>-1; cell--) {
                flag[row][cell] = COLOR_BLUE;

            }
            x--;
        }
         
        x = 1;
        rowend = (int)(heigth*0.5);
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
            x++;
        }
        x = 6;
        rowend = (int)(heigth);
        for (int row = 6; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_RED;
            }
            x--;
        }
        return flag;
    }
    public static int[][] createDNKFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;
        int cellend = 0;
        int x = 0;
        rowend = (int)(heigth*0.45);
        cellend = (int)(width*0.25);
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_RED;
        
            }
            
        }
        x = 7;
        rowend = (int)(heigth);
        for (int row = x; row<rowend; row++) {
            for (int cell =0; cell<cellend; cell++) {
                flag[row][cell] = COLOR_RED;
        
            }
            
        }
         x = 7;
        cellend = (int)(width);
        
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_RED;
        
            }
            
        }
         x = 6;
        for (int row = x; row<rowend; row++) {
            for (int cell =x+1; cell<cellend; cell++) {
                flag[row][cell] = COLOR_RED;
        
            }   
        }

          x = 7;
        for (int row = 0; row<rowend; row++) {
            for (int cell =5; cell<7; cell++) {
                flag[row][cell] = COLOR_WHITE;
        
            }
           
        }
        
        for (int row = 5; row<7; row++) {
            for (int cell =0; cell<cellend; cell++) {
                flag[row][cell] = COLOR_WHITE;
        
            }
        }
        return flag;
        }

        public static int[][] createFINFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;
        int cellend = 0;
        int x = 0;
        rowend = (int)(heigth*0.4);
        cellend = (int)(width*0.2);
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        x = 8;
        rowend = (int)(heigth);
        for (int row = x; row<rowend; row++) {
            for (int cell =0; cell<cellend; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        rowend = (int)(heigth*0.4);
        cellend = (int)(width);
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        rowend = (int)(heigth);
        for (int row = x; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
         x = 4;
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<8; cell++) {
                flag[row][cell] = COLOR_BLUE;
            }
        }
        for (int row = 4; row<8; row++) {
            for (int cell =0; cell<cellend; cell++) {
                flag[row][cell] = COLOR_BLUE;
            }
        }
        return flag;
        }

        public static int[][] createNORFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;
        int cellend = 0;
        int x = 0;
        rowend = (int)(heigth*0.4);
        cellend = (int)(width*0.2);
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_RED;
            }
        }
        x = 8;
        cellend = (int)(width);
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_RED;
            }
        }
        rowend = (int)(heigth);
        cellend = (int)(width*0.2);
        for (int row = x; row<rowend; row++) {
            for (int cell =0; cell<cellend; cell++) {
                flag[row][cell] = COLOR_RED;
            }
        }
        cellend = (int)(width);
        for (int row = x; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_RED;
            }
        }
        x = 4;
        rowend = (int)(heigth*0.4);
        for (int row = x; row<x+1; row++) {
            for (int cell =0; cell<5; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        x = 7;
        for (int row = x; row<x+1; row++) {
            for (int cell =0; cell<5; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        x = 4;
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<x+1; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        rowend = (int)(heigth);
        for (int row = 8; row<rowend; row++) {
            for (int cell =x; cell<x+1; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
           x = 7;
        rowend = (int)(heigth*0.4);
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<x+1; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        rowend = (int)(heigth);
        for (int row = 8; row<rowend; row++) {
            for (int cell =x; cell<x+1; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        for (int row = x; row<x+1; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        for (int row = 4; row<5; row++) {
            for (int cell =x; cell<cellend; cell++) {
                flag[row][cell] = COLOR_WHITE;
            }
        }
        x = 5;
        for (int row = 0; row<rowend; row++) {
            for (int cell =x; cell<x+2; cell++) {
                flag[row][cell] = COLOR_BLUE;
            }
        }
        for (int row = x; row<x+2; row++) {
            for (int cell =0; cell<cellend; cell++) {
                flag[row][cell] = COLOR_BLUE;
            }
        }
        return flag;
    }
    
    public static int[][] createKWTFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;
        int cellend = 0;
        int x = 0;
        rowend = (int)(heigth*0.5);
        cellend = (int)(width);
        
        for (int row = 0; row<rowend; row++) {
            for (int cell =0; cell<6; cell ++) {
                flag[row][cell] = COLOR_BLACK;

            }
            x++;
        }
        x = 5;
        for (int row = x; row<x+2; row++) {
            for (int cell =0; cell<x; cell ++) {
                flag[row][cell] = COLOR_BLACK;

            }
        }
        x = 4;
        rowend = (int)(heigth);
         for (int row = 7; row<rowend; row++) {
            for (int cell =x; cell>-1; cell --) {
                flag[row][cell] = COLOR_BLACK;

            }
            x--;
        }
        x = 1;
         for (int row = 0; row<5;row++) {
            for (int cell =x; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_GREEN;

            }
            x++;
        }
        x = 4;
         for (int row = x; row<8; row++) {
            for (int cell =5; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_WHITE;

            }
        }
         for (int row = 8; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_RED;

            }
            x--;
        } 
        return flag;
    }
         public static int[][] createZAFFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int rowend =0;
        int cellend = 0;
        cellend = (int)(width);
        rowend = (int)(heigth);
        int x = 2;
        for (int row = 0; row<4; row++) {
            for (int cell =x; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_RED;

            }
            x++;
        }
        x = 5;

        for (int row = 8; row<rowend; row++) {
            for (int cell =x; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_BLUE;

            }
            x--;
        }
         x = 6;
        for (int row = 4; row<5; row++) {
            for (int cell =x; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_WHITE;

            }
        }
        for (int row = 7; row<8; row++) {
            for (int cell =x; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_WHITE;

            }
        }
        x = 0;
        for (int row = 0; row<5; row++) {
            for (int cell =x; cell<6; cell ++) {
                if (row+1 == cell) {
                flag[row][cell] = COLOR_WHITE;
                }
            }
            x--;
        }
         x = 10;
        for (int row = rowend; row>6; row--) {
            for (int cell =1; cell<6; cell ++) {
                if (row-x-2 == cell) {
                flag[row][cell] = COLOR_WHITE;
                }
            }
            x-= 2;   
        }
         x = 0;
        for (int row = 0; row<6; row++) {
            for (int cell =x; cell<7; cell ++) {
                if (row == cell) {
                flag[row][cell] = COLOR_GREEN;
                }
            }
            x--;
        }
        for (int row = 1; row<6; row++) {
            for (int cell =x; cell<7; cell ++) {
                if (row == cell+1) {
                flag[row][cell] = COLOR_GREEN;
                }
            }
            x--;
        }
        
          x = 9;
        for (int row = rowend; row>5; row--) {
            for (int cell =0; cell<6; cell ++) {
                if (row-x-4 == cell) {
                flag[row][cell] = COLOR_GREEN;
                }
            }
            x-= 2;
        }
        for (int row = rowend-1; row>5; row--) {
            for (int cell =0; cell<6; cell ++) {
                if (row-x-3 == cell) {
                flag[row][cell] = COLOR_GREEN;
                }
            }
            x-= 2;
        }
        for (int row =5; row<7; row++) {
            for (int cell =6; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_GREEN;
            }
        }
         x = 0;
        for (int row = 2; row<6; row++) {
            for (int cell =x; cell<5; cell ++) {
                if (row == cell+2) {
                flag[row][cell] = COLOR_YELLOW;
                }
            }
            x--;
        }
        x = 9;
        for (int row = x; row>5; row--) {
            for (int cell =0; cell<5; cell ++) {
                if (row-x == cell) {
                flag[row][cell] = COLOR_YELLOW;
                }
            }
            x-= 2;
        }
         x = 1;
        for (int row = 3; row<6; row++) {
            for (int cell =0; cell<x; cell ++) {
                flag[row][cell] = COLOR_BLACK;

            }
            x++;
        }
         x = 3;
        for (int row = 6; row<9; row++) {
            for (int cell =x-1; cell>-1; cell--) {
                flag[row][cell] = COLOR_BLACK;

            }
            x--;
        }
        return flag;
    }
     public static int[][] createCHEFlag (int heigth, int width) {
        int [][] flag = new int[heigth][width];
        int cellend = 0;
        cellend = (int)(width);
        
        for (int row = 0; row<1; row++) {
            for (int cell =0; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_RED;

            }
        }
        for (int row = 1; row<4; row++) {
            for (int cell =0; cell<8; cell ++) {
                flag[row][cell] = COLOR_RED;

            }
        }
        for (int row = 1; row<4; row++) {
            for (int cell =12; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_RED;

            }
        }
        for (int row = 1; row<4; row++) {

            for (int cell =8; cell<12; cell ++) {
                flag[row][cell] = COLOR_WHITE;

            }
        }
        for (int row = 4; row<8; row++) {
            for (int cell =0; cell<5; cell ++) {
                flag[row][cell] = COLOR_RED;

            }
        }
         for (int row = 4; row<8; row++) {
            for (int cell =15; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_RED;

            }
        }
        for (int row = 4; row<8; row++) {
            for (int cell =5; cell<15; cell ++) {
                flag[row][cell] = COLOR_WHITE;

            }
        }
        for (int row = 8; row<11; row++) {
            for (int cell =0; cell<8; cell ++) {
                flag[row][cell] = COLOR_RED;

            }
        }
         for (int row = 8; row<11; row++) {
            for (int cell =12; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_RED;

            }
        }
        for (int row = 8; row<11; row++) {
            for (int cell =8; cell<12; cell ++) {
                flag[row][cell] = COLOR_WHITE;

            }
        }
        for (int row = 11; row>10; row--) {
            for (int cell =0; cell<cellend; cell ++) {
                flag[row][cell] = COLOR_RED;
            }
        }
        return flag;
    }
    public static void main(String[] args) {
    int[][] flag = {};
        
        flag = createCOLFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createVENFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createPOLFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createPANFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createCHLFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createUSAFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createCZEFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createDNKFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createFINFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createNORFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);      
        flag = createKWTFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createZAFFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
        flag = createCHEFlag(12,20);
        JOptionPaneArrays.showColorArray2D(null,flag);
    }
}