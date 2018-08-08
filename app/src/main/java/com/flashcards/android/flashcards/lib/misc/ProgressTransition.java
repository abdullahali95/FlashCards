package com.flashcards.android.flashcards.lib.misc;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionValues;
import com.transitionseverywhere.utils.IntProperty;

/**
 * Created by Abdullah Ali on 07/08/2018
 * Courtesy of: https://github.com/andkulikov/Transitions-Everywhere
 */
public class ProgressTransition extends Transition {

    private static final Property<ProgressBar, Integer> PROGRESS_PROPERTY =
            new IntProperty<ProgressBar>() {

                @Override
                public void setValue(ProgressBar progressBar, int value) {
                    progressBar.setProgress(value);
                }

                @Override
                public Integer get(ProgressBar progressBar) {
                    return progressBar.getProgress();
                }
            };
    
    private static final String PROPNAME_PROGRESS = "ProgressTransition:progress";

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof ProgressBar) {
            // save current progress in the values map
            ProgressBar progressBar = ((ProgressBar) transitionValues.view);
            transitionValues.values.put(PROPNAME_PROGRESS, progressBar.getProgress());
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues,
                                   TransitionValues endValues) {
        if (startValues != null && endValues != null && endValues.view instanceof ProgressBar) {
            ProgressBar progressBar = (ProgressBar) endValues.view;
            int start = (Integer) startValues.values.get(PROPNAME_PROGRESS);
            int end = (Integer) endValues.values.get(PROPNAME_PROGRESS);
            if (start != end) {
                // first of all we need to apply the start value, because right now
                // the view has end value
                progressBar.setProgress(start);
                // create animator with our progressBar, property and end value
                return ObjectAnimator.ofInt(progressBar, PROGRESS_PROPERTY, end);
            }
        }
        return null;
    }
}
