package it.hexamini.lcp.lcputility.canvas;

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
    private int MARGIN_RULE = 5;
    private int MARGIN_BETWEEN_SEQS = 20;
    private int MARGIN_BOTTOM = 200;
    private int RULE_SIZE = 14;
    private int TEXT_SIZE = 20;

    private int MARGIN_UP_SEQS = INCREMENT_Y + TEXT_SIZE;

    private Paint paint;

    private Tree treeSequents;
    private TreeMargin managerDistance;

    public CanvasSolve( Context context, String seqSx, String seqDx )
    {
        super(context);

        WindowManager wmManager = ( WindowManager ) context.getSystemService( Context.WINDOW_SERVICE );
        Display display = wmManager.getDefaultDisplay();

        //Deprecato ma maggiore compatibilita
        if( Build.VERSION.SDK_INT < 13 )
        {
            DISPLAY_WIDTH = display.getWidth();
            DISPLAY_HEIGHT = display.getHeight();
        }
        else
        {
            Point size = new Point();
            display.getSize( size );

            DISPLAY_WIDTH = size.x;
            DISPLAY_HEIGHT = size.y;
        }

        Solve solution = new Solve( seqSx, seqDx );
        treeSequents = solution.treeLeaf();

        paint = new Paint();
        paint.setFlags( Paint.ANTI_ALIAS_FLAG );

        paint.setTextSize( TEXT_SIZE );

        managerDistance = new TreeMargin( treeSequents.getRadice().treeSX,
                                          MARGIN_BETWEEN_SEQS,
                                          paint );
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        super.onDraw(canvas);

        setDisplayTree( canvas );
    }


    private void displayTree( Tree.Nodo pTree, TreeMargin.Nodo pManager, float centerY,
                              Canvas canvas )
    {
        if( pTree.treeSX != null )
        {
            String prSx = pTree.treeSX.getPredicate();
            String rule = pTree.getRule();

            float lenghtLine = paint.measureText( prSx );

            if( pTree.treeDX != null )
            {
                String prDx = pTree.treeDX.getPredicate();

                lenghtLine += pManager.margin + pManager.lenghtDx;

                float posSeqSx = pManager.relativeCenter -
                                 ( pManager.margin / 2 + pManager.lenghtSx );
                float posSeqDx = pManager.relativeCenter + pManager.margin / 2;

                canvas.drawLine( pManager.relativeCenter - lenghtLine / 2,
                                 centerY - MARGIN_UP_SEQS,
                                 pManager.relativeCenter + lenghtLine / 2,
                                 centerY - MARGIN_UP_SEQS,
                                 paint  );

                canvas.drawText( prSx,
                                 posSeqSx,
                                 centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y,
                                 paint );

                canvas.drawText( prDx,
                                 posSeqDx,
                                 centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y,
                                 paint );

                paint.setTextSize( RULE_SIZE );
                canvas.drawText( rule,
                                 pManager.relativeCenter + lenghtLine / 2 + MARGIN_RULE,
                                 centerY - MARGIN_UP_SEQS + RULE_SIZE / 4,
                                 paint );

                paint.setTextSize( TEXT_SIZE );

                //Nuovo centro ramo sinistro
                float centerYS = centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y;
                displayTree( pTree.treeSX, pManager.treeSX, centerYS, canvas );

                //Nuovo centro ramo destro
                float centerYD = centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y;
                displayTree( pTree.treeDX, pManager.treeDX, centerYD, canvas );
            }
            else
            {
                canvas.drawLine( pManager.relativeCenter - lenghtLine / 2,
                                 centerY - MARGIN_UP_SEQS,
                                 pManager.relativeCenter + lenghtLine / 2,
                                 centerY - MARGIN_UP_SEQS,
                                 paint );

                canvas.drawText( prSx,
                                 pManager.relativeCenter - lenghtLine / 2,
                                 centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y,
                                 paint );

                paint.setTextSize( RULE_SIZE );

                canvas.drawText( rule,
                                 pManager.relativeCenter + lenghtLine / 2 + MARGIN_RULE,
                                 centerY - MARGIN_UP_SEQS + RULE_SIZE / 4,
                                 paint  );

                paint.setTextSize( TEXT_SIZE );

                //Nuovo centro ramo sinistro
                centerY -= MARGIN_UP_SEQS + 2 * INCREMENT_Y;
                displayTree( pTree.treeSX, pManager, centerY, canvas );
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

        managerDistance.setCenter( centerX );

        //managerDistance.print( managerDistance.getRadice() );

        TreeMargin.Nodo pointerManager = managerDistance.getRadice();

        paint.setColor( Color.WHITE );
        canvas.drawPaint( paint );
        paint.setColor( Color.BLACK );

        String firstSeq = treeSeq.getPredicate();
        canvas.drawText( firstSeq,
                         centerX - paint.measureText( firstSeq ) / 2,
                         centerY,
                         paint );

        displayTree( treeSeq, pointerManager, centerY, canvas );
    }
}
