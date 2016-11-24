# Next: release 32 (3.0.0)

## Foundation

The following three issues were tested under Windows and Linux with similar results. They need testing under MAC.

1. Test [ModifiersFix]().
2. FX2D renderer is buggy under Windows and Linux. It needs testing also under mac.
3. Proscene shaders examples that are broken that used to work in Processing 3.2.1.:
    1. [PostEffects.Fxaa](https://github.com/remixlab/proscene/tree/master/examples/Demos/PostEffects) .
    2. [Bloom](https://github.com/remixlab/proscene/tree/master/examples/Demos/Bloom) doesn't show anyhting.

7. Serialize scene profiles and iFrame shapes and profiles.

## Android

1. Upload a figure depicting the gestures.
2. Fix double tap.
3. Fix OPPOSABLE_THREE_ID gesture.
4. Support 3 and 6 DOFs.
5. Implement the key agent.
6. Upstream:
    1. Register Motion and Key Events when they get support upstream, see this [issue](https://github.com/processing/processing-android/issues/246).
    2. The library data resources folder is not loaded, see this [issue](https://github.com/processing/processing-android/issues/247).
    3. Processing mouseX and mouseY are not reported correctly which makes a sketch defining several (off-screen) scenes not possible, see this [issue](https://github.com/processing/processing-android/issues/260).
    4. Processing obj PShape textures are not loaded, see this [issue](https://github.com/processing/processing-android/issues/249).

## JS

1. Set webgl matrices,
2. Test if the latest release can still be transpiled into JS code.
2. Release a first proof-of-concept (as we've just done with Android).

## Demos

1. Include @sechaparroc experiments.
2. [Platonic](http://blog.jpcarrascal.com/2016/04/platonic-solids-in-processing/) [solids](https://github.com/jpcarrascal/ProcessingPlatonicSolids).
3. A BIAS custom touch agent to handle (absolute? which sounds good for an student workshop) DOF2 events.
4. A BIAS custom touch agent to handle custom events.
