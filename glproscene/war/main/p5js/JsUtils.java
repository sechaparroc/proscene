/**
 * 
 */
package main.p5js;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayBoolean;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;

/**
 * @author cesar
 *
 */
public class JsUtils {

	
	
	
	
	  public static final PMatrix3D JavaScriptArray2Matrix3D(JavaScriptObject array){	  	
		  	return JsUtils.JavaScriptArray2Matrix3D(array,false);
		  }
	  
	  public static final JavaScriptObject  Matrix3D2JavaScriptArray(PMatrix3D matrix3D){  	
		  	return JsUtils.Matrix3D2JavaScriptArray(matrix3D,false);
		  }	  
		
	  	public native static float checkNumber(JavaScriptObject o)
	  	/*-{
	  		var retValue = 0;
	  		
	  		if(!isNaN(o) ) 	  		
	  			retValue = o;
	  		else
	  			console.error(o);
	  		
	  		return retValue ; 
		}-*/;
	  	
		public native static final PMatrix3D JavaScriptArray2Matrix3D(JavaScriptObject array, boolean rowMajor)/*-{
			
			function check(o)
			{
		  		var retValue = 0;
		  		
		  		if(!isNaN(o) ) 	  		
		  			retValue = o;
		  		else
		  			console.error(o);
		  			//retValue = -1.7320509;
		  		
		  		return retValue ; 
			}

			
			if(rowMajor){
			  
			  var o = array;
				return @main.p5js.PMatrix3D::new(FFFFFFFFFFFFFFFF)
			  (check(o.elements[0]),check( o.elements[1]), check(o.elements[2]),   check( o.elements[3]),
	           check(o.elements[4]), check(o.elements[5]), check(o.elements[6]),    check(o.elements[7]),
	           check(o.elements[8]), check(o.elements[9]), check(o.elements[10]),   check(o.elements[11]),
	           check(o.elements[12]), check(o.elements[13]), check(o.elements[14]), check(o.elements[15]) 
	           );		
			
			}else
			{	
			     var matrix3D = @main.p5js.PMatrix3D::new();					
				   matrix3D.@main.p5js.PMatrix3D::m00 = check(array[0]); 
				   matrix3D.@main.p5js.PMatrix3D::m01 = check(array[4]); 
				   matrix3D.@main.p5js.PMatrix3D::m02 = check(array[8]); 
				   matrix3D.@main.p5js.PMatrix3D::m03 = check(array[12]); 
				   matrix3D.@main.p5js.PMatrix3D::m10 = check(array[1]); 
				   matrix3D.@main.p5js.PMatrix3D::m11 = check(array[6]); 
				   matrix3D.@main.p5js.PMatrix3D::m12 = check(array[9]); 
				   matrix3D.@main.p5js.PMatrix3D::m13 = check(array[13]); 
				   matrix3D.@main.p5js.PMatrix3D::m20 = check(array[2]); 
				   matrix3D.@main.p5js.PMatrix3D::m21 = check(array[6]); 
				   matrix3D.@main.p5js.PMatrix3D::m22 = check(array[10]); 
				   matrix3D.@main.p5js.PMatrix3D::m23 = check(array[14]); 
				   matrix3D.@main.p5js.PMatrix3D::m30 = check(array[3]); 
				   matrix3D.@main.p5js.PMatrix3D::m31 = check(array[7]); 
				   matrix3D.@main.p5js.PMatrix3D::m32 = check(array[11]); 
				   matrix3D.@main.p5js.PMatrix3D::m33 = check(array[15]);			   			   
				   return matrix3D;		 
			}
			
								
		}-*/;
		
		

		
		public native static final JavaScriptObject  Matrix3D2JavaScriptArray(PMatrix3D matrix3D, boolean rowMajor)/*-{
		
			var array = new Array();
			
			if(rowMajor){
			//array row major
			   array[0] = matrix3D.@main.p5js.PMatrix3D::m00; 
			   array[1] = matrix3D.@main.p5js.PMatrix3D::m01; 
			   array[2] = matrix3D.@main.p5js.PMatrix3D::m02; 
			   array[3] = matrix3D.@main.p5js.PMatrix3D::m03; 
			   array[4] = matrix3D.@main.p5js.PMatrix3D::m10; 
			   array[5] = matrix3D.@main.p5js.PMatrix3D::m11; 
			   array[6] = matrix3D.@main.p5js.PMatrix3D::m12; 
			   array[7] = matrix3D.@main.p5js.PMatrix3D::m13; 
			   array[8] = matrix3D.@main.p5js.PMatrix3D::m20; 
			   array[9] = matrix3D.@main.p5js.PMatrix3D::m21; 
			   array[10] = matrix3D.@main.p5js.PMatrix3D::m22; 
			   array[11] = matrix3D.@main.p5js.PMatrix3D::m23; 
			   array[12] = matrix3D.@main.p5js.PMatrix3D::m30; 
			   array[13] = matrix3D.@main.p5js.PMatrix3D::m31; 
			   array[14] = matrix3D.@main.p5js.PMatrix3D::m32; 
			   array[15] = matrix3D.@main.p5js.PMatrix3D::m33;		

			}else
			{
			//array col major
			   array[0] = matrix3D.@main.p5js.PMatrix3D::m00; 
			   array[4] = matrix3D.@main.p5js.PMatrix3D::m01; 
			   array[8] = matrix3D.@main.p5js.PMatrix3D::m02; 
			   array[12] = matrix3D.@main.p5js.PMatrix3D::m03; 
			   array[1] = matrix3D.@main.p5js.PMatrix3D::m10; 
			   array[5] = matrix3D.@main.p5js.PMatrix3D::m11; 
			   array[9] = matrix3D.@main.p5js.PMatrix3D::m12; 
			   array[13] = matrix3D.@main.p5js.PMatrix3D::m13; 
			   array[2] = matrix3D.@main.p5js.PMatrix3D::m20; 
			   array[6] = matrix3D.@main.p5js.PMatrix3D::m21; 
			   array[10] = matrix3D.@main.p5js.PMatrix3D::m22; 
			   array[14] = matrix3D.@main.p5js.PMatrix3D::m23; 
			   array[3] = matrix3D.@main.p5js.PMatrix3D::m30; 
			   array[7] = matrix3D.@main.p5js.PMatrix3D::m31; 
			   array[11] = matrix3D.@main.p5js.PMatrix3D::m32; 
			   array[15] = matrix3D.@main.p5js.PMatrix3D::m33;		
			}
			
			return array;
			
		}-*/;	
		
		
		  public static final PMatrix2D JavaScriptArray2Matrix2D(JavaScriptObject array){	  	
			  	return JsUtils.JavaScriptArray2Matrix2D(array,true);
			  }
		  
		  public static final JavaScriptObject  Matrix2D2JavaScriptArray(PMatrix2D matrix2D){  	
			  	return JsUtils.Matrix2D2JavaScriptArray(matrix2D,true);
			  }	  

		
		  
		public native static final PMatrix2D JavaScriptArray2Matrix2D(JavaScriptObject array, boolean rowMajor)/*-{
			
			function check(o)
			{
		  		var retValue = 0;
		  		
		  		if(!isNaN(o) ) 	  		
		  			retValue = o;
		  		else
		  			console.error(o);
		  			//retValue = -1.7320509;
		  		
		  		return retValue ; 
			}

			
			if(rowMajor){
			  
			  var o = array;
				return @main.p5js.PMatrix2D::new(FFFFFF)
			  (check(o.elements[0]),check( o.elements[1]), check(o.elements[2]),   check( o.elements[3]),
	           check(o.elements[4]), check(o.elements[5]) 
	           );		
			
			}else
			{	
			     var matrix2D = @main.p5js.PMatrix2D::new(FFFFFF)				
				   matrix2D.@main.p5js.PMatrix2D::m00 = check(array[0]); 
				   matrix2D.@main.p5js.PMatrix2D::m01 = check(array[3]); 
				   matrix2D.@main.p5js.PMatrix2D::m02 = check(array[1]); 
				   matrix2D.@main.p5js.PMatrix2D::m10 = check(array[4]); 
				   matrix2D.@main.p5js.PMatrix2D::m11 = check(array[2]); 
				   matrix2D.@main.p5js.PMatrix2D::m12 = check(array[5]); 
		   			   
				   return matrix2D;		 
			}
			
								
		}-*/;
		
		

		
		public native static final JavaScriptObject  Matrix2D2JavaScriptArray(PMatrix2D matrix2D, boolean rowMajor)/*-{
		
			var array = new Array();
			
			if(rowMajor){
			//array row major
			   array[0] = matrix2D.@main.p5js.PMatrix2D::m00; 
			   array[1] = matrix2D.@main.p5js.PMatrix2D::m01; 
			   array[2] = matrix2D.@main.p5js.PMatrix2D::m02; 

			   array[3] = matrix2D.@main.p5js.PMatrix2D::m10; 
			   array[4] = matrix2D.@main.p5js.PMatrix2D::m11; 
			   array[5] = matrix2D.@main.p5js.PMatrix2D::m12; 
 
	

			}else
			{
			//array col major
			   array[0] = matrix2D.@main.p5js.PMatrix2D::m00; 
			   array[3] = matrix2D.@main.p5js.PMatrix2D::m01; 
			   
			   array[1] = matrix2D.@main.p5js.PMatrix2D::m02; 
			   array[4] = matrix2D.@main.p5js.PMatrix2D::m10; 
			   
			   array[2] = matrix2D.@main.p5js.PMatrix2D::m11; 
			   array[5] = matrix2D.@main.p5js.PMatrix2D::m12; 	
			}
			
			return array;
			
		}-*/;			  
		
		public static JsArrayNumber toJavaScriptArray(double[] srcArray) {
		  if (GWT.isScript()) {
		    return arrayAsJsArrayForProdMode(srcArray);
		  }
		  JsArrayNumber result = JavaScriptObject.createArray().cast();
		  for (int i = 0; i < srcArray.length; i++) {
		    result.set(i, srcArray[i]);
		  }
		  return result;
		}	
		
		public static JsArrayNumber toJavaScriptArray(float[] srcArray) {
		  if (GWT.isScript()) {
		    return arrayAsJsArrayForProdMode(srcArray);
		  }
		  JsArrayNumber result = JavaScriptObject.createArray().cast();
		  for (int i = 0; i < srcArray.length; i++) {
		    result.set(i, srcArray[i]);
		  }
		  return result;
		}
		
		public static JsArrayNumber toJavaScriptArray(short[] srcArray) {
		  if (GWT.isScript()) {
		    return arrayAsJsArrayForProdMode(srcArray);
		  }
		  JsArrayNumber result = JavaScriptObject.createArray().cast();
		  for (int i = 0; i < srcArray.length; i++) {
		    result.set(i, srcArray[i]);
		  }
		  return result;
		}  
		
		private static native JsArrayBoolean arrayAsJsArrayForProdMode(boolean[] array) /*-{
		return array;
		}-*/;
		
		
		private static native JsArrayInteger arrayAsJsArrayForProdMode(byte[] array) /*-{
		    return array;
		}-*/;
		
		
		private static native JsArrayNumber arrayAsJsArrayForProdMode(short[] array) /*-{
		    return array;
		}-*/;
		
		
		private static native JsArrayNumber arrayAsJsArrayForProdMode(double[] array) /*-{
		    return array;
		}-*/;
		
		
		private static native JsArrayNumber arrayAsJsArrayForProdMode(float[] array) /*-{
		    return array;
		}-*/;		
	
	
}
