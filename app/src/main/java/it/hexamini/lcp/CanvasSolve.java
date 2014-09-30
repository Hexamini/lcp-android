package it.hexamini.lcp;

/**
 * Created by Andrea on 26/09/2014.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import it.hexamini.lcp.lcputility.solve.core.Solve;
import it.hexamini.lcp.lcputility.solve.core.Tree;

public class CanvasSolve extends View
{
    private int DISPLAY_WIDTH = 0;
    private int DISPLAY_HEIGHT = 0;
    private int INCREMENT_Y = 5;
    private int MARGIN_BETWEEN_SEQS = 100;
    private int MARGIN_BOTTOM = 200;
    private int TEXT_SIZE = 20;

    private int MARGIN_UP_SEQS = INCREMENT_Y + TEXT_SIZE;

    private Paint paint;

    private Tree treeSequents;

    public CanvasSolve( Context context, String seqSx, String seqDx )
    {
        super( context );

        WindowManager wmManager = ( WindowManager ) context.getSystemService( Context.WINDOW_SERVICE );
        Display display = wmManager.getDefaultDisplay();

        Point size = new Point();

        //Deprecato ma maggiore compatibilita
        if( Build.VERSION.SDK_INT < 13 )
        {
            DISPLAY_WIDTH = display.getWidth();
            DISPLAY_HEIGHT = display.getHeight();
        }
        else
        {
            display.getSize( size );

            DISPLAY_WIDTH = size.x;
            DISPLAY_HEIGHT = size.y;
        }

        Solve solution = new Solve( seqSx, seqDx );
        treeSequents = solution.treeLeaf();

        paint = new Paint();
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        super.onDraw(canvas);

        Tree.Nodo pointerNodo = treeSequents.getRadice().treeSX;

        String firstSeq = pointerNodo.getPredicate();

        float centerX = ( DISPLAY_WIDTH / 2 );
        float centerY = DISPLAY_HEIGHT - MARGIN_BOTTOM;

        paint.setColor( Color.BLACK );
        paint.setTextSize( TEXT_SIZE );

        canvas.drawText( firstSeq, centerX - paint.measureText( firstSeq ) / 2, centerY, paint );

        if( pointerNodo.treeSX != null )
        {
            String prSx = pointerNodo.treeSX.getPredicate();

            float lenghtLine = paint.measureText( prSx );

            if( pointerNodo.treeDX != null )
            {
                String prDx = pointerNodo.treeDX.getPredicate();

                lenghtLine += MARGIN_BETWEEN_SEQS + paint.measureText( prDx );

                canvas.drawLine( centerX - lenghtLine / 2, centerY - MARGIN_UP_SEQS, centerX + lenghtLine / 2, centerY - MARGIN_UP_SEQS, paint  );
                canvas.drawText( prSx, centerX - lenghtLine / 2, centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y, paint );
                canvas.drawText( prDx, centerX + MARGIN_BETWEEN_SEQS / 2, centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y, paint );
            }
            else
            {
                canvas.drawLine( centerX - MARGIN_BETWEEN_SEQS, centerY - MARGIN_UP_SEQS, centerX - MARGIN_BETWEEN_SEQS + lenghtLine, centerY - MARGIN_UP_SEQS, paint  );
                canvas.drawText( prSx, centerX, centerY - INCREMENT_Y, paint );
            }
        }
    }

    private float posText( float center, int dir, String text )
    {
        return center + dir * ( paint.measureText( text ) / 2 + MARGIN_BETWEEN_SEQS );
    }
}
