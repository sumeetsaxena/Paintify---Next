package com.paintify.controllers;
import java.awt.event.MouseEvent;
import java.util.Stack;

import com.paintify.app.AppConfig;
import com.paintify.panels.ColorPuzzle;
import com.paintify.panels.ImageEditor;
import com.paintify.panels.GamePanel;

import java.awt.image.BufferedImage;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;

public class FillController extends DrawingController {

    public FillController(GamePanel viewer){
        super(viewer);
    }

    private void fillColor(int x, int y, Color col){
        Graphics graphics = getGraphics();  
        BufferedImage image  = getImage();

        Rectangle rectBounds = new Rectangle(0,0,image.getWidth(), image.getHeight());
        Stack<Point> stack=new Stack<Point>();

        stack.push(new Point(x,y));
        Color startingColor = new Color(image.getRGB(x,y));

        while (!stack.isEmpty()){
            Point currentPoint = stack.pop();
            if (stack.size()>100000) break;

            if (rectBounds.contains(currentPoint)){
                Color currentColor=new Color(image.getRGB(currentPoint.x, currentPoint.y));
                if ((Math.abs(currentColor.getGreen() - startingColor.getGreen())<30) &&
                (Math.abs(currentColor.getBlue() - startingColor.getBlue())<30) && 
                (Math.abs(currentColor.getRed() - startingColor.getRed())<30))
                {
                    image.setRGB(currentPoint.x, currentPoint.y, col.getRGB());

                    stack.push(new Point(currentPoint.x,currentPoint.y+1));
                    stack.push(new Point(currentPoint.x+1,currentPoint.y));
                    stack.push(new Point(currentPoint.x,currentPoint.y-1));
                    stack.push(new Point(currentPoint.x-1,currentPoint.y));
                }
            }
        }        
        viewer.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e){
        AppConfig config=AppConfig.getInstance();
        Color chosen = (Color)config.getConfig(AppConfig.FILL_COLOR);

        ImageEditor editor = viewer.getEditor();

        if (editor instanceof ColorPuzzle){
            ColorPuzzle ccEditor = (ColorPuzzle) editor;
            if (ccEditor.isColorMatch(e.getX(),e.getY(), chosen)){
                fillColor(e.getX(),e.getY(), chosen);
                ccEditor.showMessage("NICE , Keep Going!", Color.GREEN, 500);
            }else
                ccEditor.showMessage("Wrong Color, Try Again",Color.RED, 2000);

        }else
            fillColor(e.getX(),e.getY(), chosen);

        viewer.repaint();
    }
}

