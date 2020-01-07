/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import fun3kochfractalfx.FUN3KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author Nico Kuijpers
 * Modified for FUN3 by Gertjan Schouten
 */
public class KochManager {

    private final ExecutorService executor =  Executors.newFixedThreadPool(3);

    private KochFractal koch;
    private ArrayList<Edge> edges;
    private FUN3KochFractalFX application;
    private TimeStamp tsCalc;
    private TimeStamp tsDraw;
    LeftGenerator leftGenerator;
    BottomGenerator bottomGenerator;
    RightGenerator rightGenerator;

    public KochManager(FUN3KochFractalFX application) {
        this.edges = new ArrayList<Edge>();
        this.koch = new KochFractal();
        this.application = application;
        this.tsCalc = new TimeStamp();
        this.tsDraw = new TimeStamp();

    }
    
    public void changeLevel(int nxt) {
        this.leftGenerator = new LeftGenerator(nxt);
        this.bottomGenerator = new BottomGenerator(nxt);
        this.rightGenerator = new RightGenerator(nxt);

        edges.clear();
        leftGenerator.edges.clear();
        bottomGenerator.edges.clear();
        rightGenerator.edges.clear();
        tsCalc.init();
        tsCalc.setBegin("Begin calculating");
        List<Future> Futures = new ArrayList<Future>();

        Futures.add(executor.submit(this.leftGenerator));
        Futures.add(executor.submit(this.bottomGenerator));
        Futures.add(executor.submit(this.rightGenerator));

        try
        {
            for (Future future : Futures)
            {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }

        edges.addAll(leftGenerator.GetEdges());
        edges.addAll(bottomGenerator.GetEdges());
        edges.addAll(rightGenerator.GetEdges());

        koch.SetNrOfEdges(edges.size());

        tsCalc.setEnd("End calculating");
        application.setTextNrEdges("" + koch.getNrOfEdges());
        application.setTextCalc(tsCalc.toString());
        drawEdges();
    }
    
    public void drawEdges() {
        tsDraw.init();
        tsDraw.setBegin("Begin drawing");
        application.clearKochPanel();
        for (Edge e : edges) {
            application.drawEdge(e);
        }
        tsDraw.setEnd("End drawing");
        application.setTextDraw(tsDraw.toString());
    }
    
    public void addEdge(Edge e) {
        edges.add(e);
    }
}
