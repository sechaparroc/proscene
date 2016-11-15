# Next: release 29 (Beta.7 or 3.0.0 ?)

## Foundation

0. Find a fix for #9.
1. ~~Improve the keyboard, currently IFrame and Scene bindings are conflicting.~~
2. ~~Verify support for all the upcoming demos.~~
3. ~~Improve the whole reflective messaging system (no need to print the exception stack ever since that's for debugging). ClickShortcuts not reporting binding errors.~~
4. ~~Improve iFrame reflective algorithms API docs.~~
5. Test the new 'checkIfGrabsInput' performance.
6. ~~Make [interactiveFrame.info()](https://github.com/remixlab/proscene/blob/master/src/remixlab/proscene/InteractiveFrame.java) more generic by making it _event_ agnostic.~~
Idea: 3-step process at the Profile:
* ~~(Profile): Order the output of info() according to the Shortcut type~~
* ~~(Profile): Add the titles according to the Shortcut type~~
* ~~(Scene): Parse the result. Q: Perhaps also at the Profile?~~
7. Serialize scene profiles and iFrame shapes and profiles.

## Android

1. ~~Comment all examples describing *which gestures are supported* (depict them) and how bindings work.~~
2. Fix double tap.
3. Fix OPPOSABLE_THREE_ID gesture.
4. Support 3 and 6 DOFs.
5. Implement the key agent.
6. Register Motion and Key Events when they get support upstream, see this [issue](https://github.com/processing/processing-android/issues/246).
7. Processing mouseX and mouseY are not reported correctly which makes a skecth defining several (off-screen) scenes not possible.

## JS

1. Set webgl matrices,
2. Test if the latest release can still be transpiled into JS code.
2. Release a first proof-of-concept (as we've just done with Android).

## Demos

1. Include @sechaparroc experiments.
2. [Platonic](http://blog.jpcarrascal.com/2016/04/platonic-solids-in-processing/) [solids](https://github.com/jpcarrascal/ProcessingPlatonicSolids).
3. A BIAS custom touch agent to handle (absolute? which sounds good for an student workshop) DOF2 events.
4. A BIAS custom touch agent to handle custom events.
