ProScene
========

## Description

**ProScene** (pronounced similar as the Czech word **"prosím"** which means **"please"**) is a java library package which provides classes to ease the creation of interactive 3D scenes in [[Processing|http://processing.org/]].

**ProScene** extensively uses **interactive frames**, i.e., a coordinate system that can be controlled with any [[HID|http://en.wikipedia.org/wiki/Human_interface_device]], allowing to easily setup a 2D or 3D scene.

**ProScene** provides seemless integration with **Processing**: its API has been designed to fit that of **Processing** and its implementation has been optimized to work along side with it. It suppports all major **Processing** flavours: Desktop, JS, and Android.

**ProScene** support is led by the active and great Processing community at its [[forum|http://forum.processing.org/search/proscene]] where you can reach us.

## Key features

 * *Tested* under Linux, Mac OSX and Windows, and properly works with the P2D, and P3D Processing renderers. No special dependencies or requirements needed (apart of course from [[Processing-2.x|http://processing.org/ Processing-1.5.1]]).
 * It suppports all major **Processing** flavours: Desktop, JS, and Android.
 * API design that provides seemless integration with **Processing** (e.g., providing flexible animation and drawing mechanisms), and allows extensibility of its key features.
 * Default interactivity to your *Processing* scenes through the mouse and keyboard that simply does what you expect.
 * Generic suppport for [[Human Interface Devices|http://en.wikipedia.org/wiki/Human_interface_device]]. 
 * Arcball, walkthrough and third person camera modes.
 * Hierarchical coordinate systems (frames), with functions to convert between them.
 * Coordinate systems can easily be moved with the mouse.
 * Keyframes.
 * Object picking.
 * Keyboard shortcuts and camera profiles customization.
 * Animation framework.
 * Screen drawing (i.e., drawing of 2d primitives on top of a 3d scene).
 * Off-screen rendering mode support.
 * Handy set of complete documented examples that illustrates the use of the package
 * A complete [[reference documentation|http://www.disi.unal.edu.co/grupos/remixlab/local/projects/proscene-1.1.0/reference/index.html]].
 * Active support and continuous discussions led by the [[Processing community|http://forum.processing.org/search/proscene]].

## Origin of the name

*ProScene* not only means a *"pro-scene"*, but it is a two-phoneme word pronounced similar as the Czech word *"prosím"* (which means *"please"*), obtained by removing the middle phoneme (*"ce"*) of the word *pro-ce-ssing*. Thus, the name *"ProScene"* suggests the main goal of the package, which is to help you _shorten_ the creation of interactive 3D scenes in *Processing*.

## Usage

All library features requires a `Scene` object (which is the main package class) to be instantiated (usually within your sketch setup method). There are three ways to do that:
 # **Direct instantiation**. In this case you should instantiate your own Scene object at the `PApplet.setup()` function. 
 # **Inheritance**. In this case, once you declare a `Scene` derived class, you should implement `proscenium()` which defines the objects in your scene. Just make sure to define the `PApplet.draw()` method, even if it's empty.
 # **External draw handler registration**. You can even declare an external drawing method and then register it at the Scene with `addDrawHandler(Object, String)`. That method should return `void` and have one single `Scene` parameter. This strategy may be useful when you have the same drawing code shared among multiple viewers.

See the examples **BasicUse**, **AlternativeUse**, and **StandardCamera** for an illustration of these techniques. To get start using the library and learn its main features, have a look at the complete set of well documented examples that come along with it. Other uses are also covered in the example set and include (but are not limited to): drawing mechanisms, animation framework, and camera and keyboard customization. Advanced users may take full advantage of the fully documented [http://www.disi.unal.edu.co/grupos/remixlab/local/projects/proscene-1.1.0/reference/index.html API reference] (which is also included in the package file).

# Acknowledgements

To come...

# Author, core developer and maintainer

[[Jean Pierre Charalambos|http://disi.unal.edu.co/profesores/pierre/]], [[National University of Colombia|http://www.unal.edu.co]]