# Next release: 32 (3.0.0)

## Foundation

The following three issues were tested with Processing-3.2.3 under Windows and Linux and they give similar results. They need testing under MAC.

1. Proscene shaders examples that are broken that used to work in Processing 3.2.1.:
    1. [PostEffects.Fxaa](https://github.com/remixlab/proscene/tree/master/examples/Demos/PostEffects) gives a blank screen, even after removing the sampler2D define in the shader and using a uniform instead, as suggested by @codeanticode.
    2. [Bloom](https://github.com/remixlab/proscene/tree/master/examples/Demos/Bloom) displays the scene but doesn't apply the effect.

## Android

### Upstream (Processing Android related)

1. Register Motion and Key Events when they get support upstream, see this [issue](https://github.com/processing/processing-android/issues/246).
2. The library data resources folder is not loaded, see this [issue](https://github.com/processing/processing-android/issues/247).
3. Processing mouseX and mouseY are not reported correctly which makes a sketch defining several (off-screen) scenes not possible, see this [issue](https://github.com/processing/processing-android/issues/260).
4. Processing obj PShape textures are not loaded, see this [issue](https://github.com/processing/processing-android/issues/249).

### Downstream (Proscene-android)

1. Upload a figure depicting the gestures.
2. Fix double tap.
3. Fix OPPOSABLE_THREE_ID gesture.
4. Support 3 and 6 DOFs.
5. Implement the key agent.

## JS

1. Port test each sub-tree.
* events (bias.js)
* timing (fpstiming.js)
* scene graph (proscene.js)

## Demos

1. [Platonic](http://blog.jpcarrascal.com/2016/04/platonic-solids-in-processing/) [solids](https://github.com/jpcarrascal/ProcessingPlatonicSolids).
2. A BIAS custom touch agent to handle (absolute? which sounds good for an student workshop) DOF2 events.
3. A BIAS custom touch agent to handle custom events.

# Future releases

## Foundation

1. Release inverse kinematics.
2. Serialize scene profiles and iFrame shapes and profiles.
3. Perform a deep copy of generic-children frames.
