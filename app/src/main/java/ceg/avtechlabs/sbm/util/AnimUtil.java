package ceg.avtechlabs.sbm.util;

import android.animation.Animator;
import android.os.Build;
import android.transition.Fade;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.PathInterpolator;

/**
 * Created by adhithyan-3592 on 18/01/17.
 */

public class AnimUtil {
  public static void showRevealEffect(final View v, int x, int y, Animator.AnimatorListener
      listener) {
    v.setVisibility(View.VISIBLE);

    int height = v.getHeight();

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Animator animator = ViewAnimationUtils.createCircularReveal(v, x, y, 0, height);
      animator.setDuration(350);
      animator.addListener(listener);
      animator.start();
    }
  }

  public static void configureWindowEnterExitTransition(Window w) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Fade fade = new Fade();
      fade.setInterpolator(new PathInterpolator(0.4f, 0, 1, 1));
      fade.setDuration(5000);

      w.setEnterTransition(fade);
      w.setExitTransition(fade);
    }
  }
}
