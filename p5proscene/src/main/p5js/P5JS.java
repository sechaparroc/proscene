package main.p5js;



import main.eventjs.JsEventHandler;
import remixlab.bias.Agent;
import remixlab.dandelion.geom.Mat;

import com.google.gwt.core.client.JavaScriptObject;

public class P5JS {
	
	
	private static  JavaScriptObject p5js;
	  public static final int CENTER   = 3;

	  // key will be CODED and keyCode will be this value
	  public static final int UP        = 0x26;
	  public static final int DOWN      = 0x28;
	  public static final int LEFT      = 0x25;
	  public static final int RIGHT     = 0x27;
	  public static final int WHEEL = 8;  
	
	
	public  P5JS (){}
	public  P5JS (JavaScriptObject ctx)
	{
		init(ctx);
	}
	
	
	JsEventHandler jsHandler = new 	JsEventHandler();
	
	private native void init(JavaScriptObject object)/*-{

		@main.p5js.P5JS::p5js = object;

	}-*/;
	
	
	public native int width() /*-{
	var p5js = @main.p5js.P5JS::p5js
	return p5js.width;	

	}-*/;	
	
	public native int height() /*-{
		var p5js = @main.p5js.P5JS::p5js
		return p5js.height;
	}-*/;
	
	
	public PMatrix3D projmodelview() {
		PMatrix3D retValue = new PMatrix3D();
		retValue.set(projection());
		retValue.apply(modelview());
		return retValue;
	}
	
	
	public PMatrix3D modelview() {
		return getModelViewMatrix();
	
	}
	
	public PMatrix3D projection() {
		return getProjectionMatrix();
	
	}	
	
	
	public final native PMatrix3D getProjectionMatrix()/*-{
	console.log("getProjectionMatrix");
		var p5js = @main.p5js.P5JS::p5js;

		var obj = p5js.getProjectionMatrix();
	
	
		return @main.p5js.PMatrix3D::new(FFFFFFFFFFFFFFFF)( obj[0], obj[1], obj[2], obj[3], 
															obj[4], obj[5], obj[6], obj[7], 
															obj[8], obj[9], obj[10], obj[11],
															obj[12], obj[13], obj[14], obj[15]);
	
	
	}-*/;
	
	
	
	private  final native PMatrix3D getModelViewMatrix()/*-{
//		console.log("getModelViewMatrix");
		var p5js = @main.p5js.P5JS::p5js;
	
		var obj = p5js.getModelViewMatrix();

		
		
		return @main.p5js.PMatrix3D::new(FFFFFFFFFFFFFFFF)( obj[0], obj[1], obj[2], obj[3], 
															obj[4], obj[5], obj[6], obj[7], 
															obj[8], obj[9], obj[10], obj[11],
															obj[12], obj[13], obj[14], obj[15]);		
			
	}-*/;	
	
	
	
	public    PMatrix getMatrix() {
		return getModelViewMatrix();	
	
	}
	
	public native void translate(float f, float g) /*-{
	var p5js = @main.p5js.P5JS::p5js;
	p5js.translate( f,  g);	

}-*/;
	
	public native void translate(float x, float y, float z) /*-{
	var p5js = @main.p5js.P5JS::p5js;
	//console.log("translate");
	p5js.translate( x,  y,  z) ;	

}-*/;	
	
	
	public  native void rotate(float angle) /*-{
	var p5js = @main.p5js.P5JS::p5js;
	p5js.rotate( angle);	

}-*/;
	
	
	
	public native  void rotateX(float angle) /*-{
		var p5js = @main.p5js.P5JS::p5js;
		p5js.rotateX( angle) ;	
	
	}-*/;	
	
	public native void rotateY(float angle) /*-{
		var p5js = @main.p5js.P5JS::p5js;
		p5js.rotateY( angle);	
	
	}-*/;
	
	public  native void rotateZ(float angle)/*-{
		var p5js = @main.p5js.P5JS::p5js;
		p5js.rotateZ( angle);	
	
	}-*/;

	
	public  native void rotate(float angle, float x, float y, float z) /*-{
		var p5js = @main.p5js.P5JS::p5js;
		p5js.rotate( angle,  x,  y,  z);	
	
	}-*/;
	
	
	public  native void scale(float x) /*-{
		var p5js = @main.p5js.P5JS::p5js;
		p5js.scale( x);	
	
	}-*/;
	public  native void scale(float x, float y) /*-{
		var p5js = @main.p5js.P5JS::p5js;
		p5js.scale( x,  y);	
	
	}-*/;
	
	public  native void scale(float x, float y, float z) /*-{
		var p5js = @main.p5js.P5JS::p5js;
		p5js.scale( x,  y,  z);	
	
	}-*/;
	
	
	public  native void setProjection(PMatrix3D pMatrix) /*-{
	
console.log("setProjection");
		var obj = @main.p5js.JsUtils::Matrix3D2JavaScriptArray(Lmain/p5js/PMatrix3D;)(pMatrix);
	
		var p5js = @main.p5js.P5JS::p5js;
	
	
	
		p5js.setProjectionMatrix( obj[0], obj[1], obj[2], obj[3], 
								obj[4], obj[5], obj[6], obj[7], 
								obj[8], obj[9], obj[10], obj[11],
								obj[12], obj[13], obj[14], obj[15])
	

	
	}-*/;
	
	
	public  native void setModelView(PMatrix3D pMatrix) /*-{
	console.log("setModelView");
	
		var obj = @main.p5js.JsUtils::Matrix3D2JavaScriptArray(Lmain/p5js/PMatrix3D;)(pMatrix);
	
		var p5js = @main.p5js.P5JS::p5js;
	
	
	
		p5js.setModelViewMatrix( obj[0], obj[1], obj[2], obj[3], 
								obj[4], obj[5], obj[6], obj[7], 
								obj[8], obj[9], obj[10], obj[11],
								obj[12], obj[13], obj[14], obj[15]);
	


	}-*/;
	
	
	
	
	public  native void setMatrix(PMatrix3D pMatrix) /*-{
	console.log("setMatrix");
		var obj = @main.p5js.JsUtils::Matrix3D2JavaScriptArray(Lmain/p5js/PMatrix3D;)(pMatrix);	
	
		var p5js = @main.p5js.P5JS::p5js;
		p5js.resetMatrix();
	
		p5js.applyMatrix( obj[0], obj[1], obj[2], obj[3], 
						obj[4], obj[5], obj[6], obj[7], 
						obj[8], obj[9], obj[10], obj[11],
						obj[12], obj[13], obj[14], obj[15]);
	
	}-*/;
	
	public native void popMatrix() /*-{
		var p5js = @main.p5js.P5JS::p5js;
		//p5js.popMatrix(); // -> not implemented in p5.js , use pop()
		
			p5js.pop(); 
	
	}-*/;
		
	public  native void fill(int i, int j, int k)/*-{
		var p5js = @main.p5js.P5JS::p5js;
		p5js.fill( i,  j,  k);	
	
	}-*/;
	
	
	public  native void fill(int i, int j, int k, int l) /*-{
		var p5js = @main.p5js.P5JS::p5js;
		p5js.fill( i,  j,  k , l);	
	
	}-*/;
	
	public native void pushMatrix() /*-{
		var p5js = @main.p5js.P5JS::p5js;
		
		
		//p5js.pushMatrix();// -> not implemented in p5.js , use push()
	
		p5js.push();	
	}-*/;
	
	
	public native void background(int rgb) /*-{
		var p5js = @main.p5js.P5JS::p5js;			
		p5js.background( rgb);			
	
	}-*/;





	
  public  native void rect(float a, float b, float c, float d) /*-{
		var p5js = @main.p5js.P5JS::p5js;			
		p5js.rect( a,  b,  c,  d);	
	}-*/;
  
  public  native void rect(float a, float b, float c, float d, float r) /*-{
		var p5js = @main.p5js.P5JS::p5js;			
		p5js.rect( a,  b,  c,  d,  r);	

	}-*/;
  
  
  public  native void rect(float a, float b, float c, float d,
          float tl, float tr, float br, float bl) /*-{
		var p5js = @main.p5js.P5JS::p5js;		
			
		p5js.rect( a,  b,  c,  d,  tl,  tr,  br,  bl);		

	}-*/;
  
  
  
	private  native JavaScriptObject getCanvas() /*-{
	
	var p5js = @main.p5js.P5JS::p5js;	
			
		return p5js._renderer.canvas;
		
	}-*/;
	
  
	public void registerMethod(String methodName, Agent motionAgent) {

		if(methodName.contains("mouse"))
			jsHandler.addMouseAgent(getCanvas(), motionAgent, methodName);
		else
			jsHandler.addKeyAgent(getCanvas(), motionAgent, methodName);
	}  
  
  public  native  void registerMethod(String methodName, Object targetScene) /*-{
  	
  		
  	//	$wnd.targetScenePre =   $entry(targetScene.@main.p5js.TargetScene::pre());
  	//	$wnd.targetScenedraw =   $entry(targetScene.@main.p5js.TargetScene::draw());
  		
  		
		var p5js = @main.p5js.P5JS::p5js;		
				
		
		if(methodName == "pre")
		{
			p5js._predraw = function() {
				

  				@main.eventjs.Reflect::ExecuteEvent(Ljava/lang/Object;Ljava/lang/String;)(targetScene,methodName);
			};
		}
		else if(methodName == "draw")
		{
			p5js._postdraw = function() {
  				@main.eventjs.Reflect::ExecuteEvent(Ljava/lang/Object;Ljava/lang/String;)(targetScene,methodName);
			};
		}

		
		
	}-*/;


  public static  native void console(String a) /*-{
		console.log(a); ;	
	}-*/;
  
  
  public static  native void console(float a) /*-{
		console.log(a) ;	
	}-*/;
  


  
  


			public  native void  sphere(int i) /*-{
					var p5js = @main.p5js.P5JS::p5js;	
							
					p5js.sphere(i);
				}-*/;
			

			public  native void  box(int i) /*-{
					var p5js = @main.p5js.P5JS::p5js;								
					p5js.box(i);
					
					
				}-*/;

			
			public  native void pushProjection() /*-{
				
			var p5js = @main.p5js.P5JS::p5js;	
					
				p5js._renderer.pushProjection();
				
			}-*/;
	
			public  native void popProjection() /*-{
				
			var p5js = @main.p5js.P5JS::p5js;	
					
				p5js._renderer.popProjection();
				
			}-*/;
			
			

			public  native void resetProjection() /*-{
			
			var p5js = @main.p5js.P5JS::p5js;	
					
				p5js._renderer.resetProjection();
				
			}-*/;
			
			public void printProjection() {
				// TODO Auto-generated method stub
				
			}
			
			public  native void applyProjection(PMatrix3D pMatrix)/*-{
					console.log(a); 
			var p5js = @main.p5js.P5JS::p5js;	
			gl.applyProjection( pMatrix);	
		
		}-*/;
			


			
			public  native void resetMatrix()/*-{
				
				var p5js = @main.p5js.P5JS::p5js;	
				gl.resetMatrix( );	
			
			}-*/;
			
			
			public native void printModelView() /*-{
				var p5js = @main.p5js.P5JS::p5js;
				
				p5js.printMatrix();
			}-*/;
			
			

			
			
			
			public  native void applyMatrix(PMatrix3D pMatrix) /*-{
			

			var obj = @main.p5js.JsUtils::Matrix3D2JavaScriptArray(Lmain/p5js/PMatrix3D;)(pMatrix);
		
			var p5js = @main.p5js.P5JS::p5js;
		
		
				p5js.applyMatrix( obj[0], obj[1], obj[2], obj[3], 
								obj[4], obj[5], obj[6], obj[7], 
								obj[8], obj[9], obj[10], obj[11],
								obj[12], obj[13], obj[14], obj[15]);
		
		}-*/;
			public native  void perspective(float fieldOfView, float aspectRatio,
					float zNear, float zFar) /*-{
					
					var p5js = @main.p5js.P5JS::p5js;	
					p5js.perspective( fieldOfView,  aspectRatio, zNear,  zFar) ;	
				
				}-*/;
			public native  void ortho(float f, float g, float h, float i, float zNear,
					float zFar)/*-{
					
					var p5js = @main.p5js.P5JS::p5js;	
					p5js.ortho( f,  g,  h,  i,  zNear, zFar);	
				
				}-*/;
			public native void camera(float x, float y, float z, float x2, float y2,
					float z2, float x3, float y3, float z3) /*-{
					
					var p5js = @main.p5js.P5JS::p5js;	
					p5js.camera( x,  y,  z,  x2,  y2, z2,  x3,  y3,  z3) ;	
				
				}-*/;
			
			public native void cylinder(int i, int j)/*-{
			
			var p5js = @main.p5js.P5JS::p5js;	
			p5js.cylinder(i,j );	
		
		}-*/;
			public void printMatrix() {
				// TODO Auto-generated method stub
				
			}

}
