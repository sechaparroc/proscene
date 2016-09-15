# Next: release 29 (Beta.7 or 3.0.0 ?)

## Foundation

1. Support for all the upcoming demos
2. ~~Improve the whole reflective messaging system (no need to print the exception stack ever since that's for debugging). ClickShortcuts not reporting binding errors.~~
3. Improve iFrame reflective algorithms API docs.
4. Test the new 'checkIfGrabsInput' performance.
5. Make [interactiveFrame.info()](https://github.com/remixlab/proscene/blob/master/src/remixlab/proscene/InteractiveFrame.java) more generic by making it _event_ agnostic.
Idea: 3-step process at the Profile:
* ~~(Profile): Order the output of info() according to the Shortcut type~~
* ~~(Profile): Add the titles according to the Shortcut type~~
* (Scene): Parse the result. Q: Perhaps also at the Profile?

## Android

1. Comment all examples describing *which gestures are supported* (depict them) and how bindings work.
2. Fix double tap.
3. Support 3 and 6 DOFs.
4. Implement the key agent.
5. Register Motion and Key Events when they get support upstream, see thie [issue](https://github.com/processing/processing-android/issues/246).

## JS

1. Set webgl matrices,
2. Test if the latest release can still be transpiled into JS code.
2. Release a first proof-of-concept (as we've just done with Android).

## Demos

1. Include @sechaparroc experiments.
2. [Platonic](http://blog.jpcarrascal.com/2016/04/platonic-solids-in-processing/) [solids](https://github.com/jpcarrascal/ProcessingPlatonicSolids).
3. A BIAS custom touch agent to handle (absolute? which sounds good for an student workshop) DOF2 events.
4. A BIAS custom touch agent to handle custom events.
