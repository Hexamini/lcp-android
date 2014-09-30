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
    private int MARGIN_BOTTOM = 0;

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

        System.out.println( DISPLAY_WIDTH + " " + DISPLAY_HEIGHT );

        MARGIN_BOTTOM = 200;

        Solve solution = new Solve( seqSx, seqDx );
        treeSequents = solution.treeLeaf();

        paint = new Paint();
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        super.onDraw(canvas);



        float centerX = ( DISPLAY_WIDTH / 2 ) - paint.measureText( treeSequents.getRadice().treeSX.info.getPredicate() );
        float centerY = DISPLAY_HEIGHT - MARGIN_BOTTOM;

        paint.setColor( Color.BLACK );
        paint.setTextSize( 20 );

        canvas.drawText( treeSequents.getRadice().treeSX.info.getPredicate(), centerX, centerY, paint );
    }
}
