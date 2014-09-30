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
    private int MARGIN_RULE = 10;
    private int MARGIN_BOTTOM = 200;
    private int RULE_SIZE = 16;
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
        paint.setFlags( Paint.ANTI_ALIAS_FLAG );
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        super.onDraw(canvas);

        Tree.Nodo treeSeq = treeSequents.getRadice().treeSX;

        String firstSeq = treeSeq.getPredicate();

        float centerX = ( DISPLAY_WIDTH / 2 );
        float centerY = DISPLAY_HEIGHT - MARGIN_BOTTOM;

        paint.setColor( Color.BLACK );
        paint.setTextSize( TEXT_SIZE );

        canvas.drawText( firstSeq, centerX - paint.measureText( firstSeq ) / 2, centerY, paint );
        displayTree( treeSeq, centerX, centerY, canvas );
    }

    private void displayTree( Tree.Nodo p, float centerX, float centerY, Canvas canvas )
    {
        if( p.treeSX != null )
        {
            String prSx = p.treeSX.getPredicate();
            String rule = p.getRule();

            float lenghtLine = paint.measureText( prSx );

            if( p.treeDX != null )
            {
                String prDx = p.treeDX.getPredicate();

                lenghtLine += MARGIN_BETWEEN_SEQS + paint.measureText( prDx );

                canvas.drawLine( centerX - lenghtLine / 2, centerY - MARGIN_UP_SEQS, centerX + lenghtLine / 2, centerY - MARGIN_UP_SEQS, paint  );
                canvas.drawText( prSx, centerX - lenghtLine / 2, centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y, paint );
                canvas.drawText( prDx, centerX + MARGIN_BETWEEN_SEQS / 2, centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y, paint );

                paint.setTextSize( RULE_SIZE );
                canvas.drawText( rule, centerX + lenghtLine / 2 + MARGIN_RULE, centerY - MARGIN_UP_SEQS + RULE_SIZE / 4, paint  );

                paint.setTextSize( TEXT_SIZE );

                //Nuovo centro ramo sinistro
                float centerXS = centerX - lenghtLine / 4;
                float centerYS = centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y;
                displayTree( p.treeSX, centerXS, centerYS, canvas );

                //Nuovo centro ramo destro
                float centerXD = centerX + lenghtLine / 4;
                float centerYD = centerY - MARGIN_UP_SEQS - 2 * INCREMENT_Y;
                displayTree( p.treeDX, centerXD, centerYD, canvas );
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
                displayTree( p.treeSX, centerX, centerY, canvas );
            }
        }
    }
}
