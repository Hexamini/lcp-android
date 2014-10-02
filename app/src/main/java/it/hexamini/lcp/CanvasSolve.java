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

import java.util.ArrayList;

import it.hexamini.lcp.lcputility.canvas.DistanceSeqs;
import it.hexamini.lcp.lcputility.canvas.ErrorDistance;
import it.hexamini.lcp.lcputility.solve.core.Solve;
import it.hexamini.lcp.lcputility.solve.core.Tree;

public class CanvasSolve extends View
{
    private DistanceSeqs managerDistance;

    private int DISPLAY_WIDTH = 0;
    private int DISPLAY_HEIGHT = 0;
    private int INCREMENT_Y = 5;
    private int MARGIN_RULE = 10;
    private int MARGIN_BETWEEN_SEQS = 30;
    private int MARGIN_BOTTOM = 200;
    private int RULE_SIZE = 14;
    private int TEXT_SIZE = 20;

    private int MARGIN_UP_SEQS = INCREMENT_Y + TEXT_SIZE;

    private Paint paint;

    private Tree treeSequents;

    public CanvasSolve( Context context, String seqSx, String seqDx )
    {
        super(context);

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

        managerDistance = new DistanceSeqs( MARGIN_BETWEEN_SEQS );

        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        super.onDraw(canvas);
        setDisplayTree( canvas );
    }

    private void displayTree( Tree.Nodo p, float centerX, float centerY, Canvas canvas, int level )
    {
        if( p.treeSX != null )
        {
            level++;

            String prSx = p.treeSX.getPredicate();
            String rule = p.getRule();

            float lenghtLine = paint.measureText( prSx );

            if( p.treeDX != null )
            {
                String prDx = p.treeDX.getPredicate();

                try
                {
                    int margin = managerDistance.getDistance( paint.measureText( prSx ), paint.measureText( prDx ), centerX  );

                    System.out.println( "Main: " + level + " " + margin );

                    lenghtLine += 2 * margin + paint.measureText( prDx );

                    canvas.drawLine( centerX - lenghtLine / 2, centerY - MARGIN_UP_SEQS, centerX + lenghtLine / 2, centerY - MARGIN_UP_SEQS, paint  );
                    canvas.drawText( prSx, centerX - lenghtLine / 2, centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y, paint );
                    canvas.drawText( prDx, centerX + margin, centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y, paint );

                    paint.setTextSize( RULE_SIZE );
                    canvas.drawText( rule, centerX + lenghtLine / 2 + MARGIN_RULE, centerY - MARGIN_UP_SEQS + RULE_SIZE / 4, paint  );

                    paint.setTextSize( TEXT_SIZE );

                    //Nuovo centro ramo sinistro
                    float centerXS = centerX - ( paint.measureText( prSx ) / 2 + margin );
                    float centerYS = centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y;
                    displayTree( p.treeSX, centerXS, centerYS, canvas, level );

                    //Nuovo centro ramo destro
                    float centerXD = centerX + ( paint.measureText( prDx ) / 2 + margin );
                    float centerYD = centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y;
                    displayTree( p.treeDX, centerXD, centerYD, canvas, level );
                }
                catch( ErrorDistance e )
                {
                    //Esco dalla ricorsione e ricomincio da capo
                    managerDistance.rewindStart();
                    setDisplayTree( canvas );
                }
            }
            else
            {
                canvas.drawLine( centerX - lenghtLine / 2, centerY - MARGIN_UP_SEQS, centerX + lenghtLine / 2, centerY - MARGIN_UP_SEQS, paint );
                canvas.drawText( prSx, centerX - lenghtLine / 2, centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y, paint );

                paint.setTextSize( RULE_SIZE );
                canvas.drawText( rule, centerX + lenghtLine / 2 + MARGIN_RULE, centerY - MARGIN_UP_SEQS + RULE_SIZE / 4, paint  );

                paint.setTextSize( TEXT_SIZE );

                //Nuovo centro ramo sinistro
                centerY -= MARGIN_UP_SEQS + 2 * INCREMENT_Y;
                displayTree( p.treeSX, centerX, centerY, canvas, level );
            }
        }
    }

    /**
     * @param canvas : Tela dove disegnare l'albero
     */
    private void setDisplayTree( Canvas canvas )
    {
        Tree.Nodo treeSeq = treeSequents.getRadice().treeSX;

        float centerX = ( DISPLAY_WIDTH / 2 );
        float centerY = DISPLAY_HEIGHT - MARGIN_BOTTOM;

        paint.setColor( Color.WHITE );
        canvas.drawPaint( paint );
        paint.setColor( Color.BLACK );
        paint.setTextSize( TEXT_SIZE );

        String firstSeq = treeSeq.getPredicate();
        canvas.drawText( firstSeq, centerX - paint.measureText( firstSeq ) / 2, centerY, paint );

        displayTree( treeSeq, centerX, centerY, canvas, 0 );
    }
}
