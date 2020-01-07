package calculate;

import java.util.ArrayList;
import java.util.List;

public class BottomGenerator implements Runnable
{
    KochFractal koch;
    public List<Edge> edges = new ArrayList<>();


    public BottomGenerator(int level)
    {
        this.koch = new KochFractal();
        koch.setLevel(level);
    }

    @Override
    public void run()
    {
        try{
            koch.generateBottomEdge();
            edges = koch.GetEdges();
        }
        catch (Exception ex){

        }
    }

    public List<Edge> GetEdges(){
        return edges;
    }
}
