/* I declare that this code is my own work*/
/*Author <Zixuan Zhang> <zzhang120@sheffield.ac.uk>*/

import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
  
public class Anilamp_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
    
  public Anilamp_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4.5f,8f,23f));
    //this.camera.setTarget(new Vec3(0f, 15, 0f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(30, aspect));
  }

  /* Draw ,same as the lab exercise sheets*/
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary, similiar as the lab exercise sheets */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    light.dispose(gl);
    floor.dispose(gl);
    sphere.dispose(gl);
    cube.dispose(gl);
    wall.dispose(gl);
    tabletop.dispose(gl);
    tableleg1.dispose(gl);
    tableleg2.dispose(gl);
    tableleg3.dispose(gl);
    tableleg4.dispose(gl);
    base1.dispose(gl);
    lowerArm1.dispose(gl);
    tail1.dispose(gl);
    tail21.dispose(gl);
    upperArm1.dispose(gl);
    head1.dispose(gl);
    nose1.dispose(gl);
    ear1.dispose(gl);
    ear21.dispose(gl);
    book.dispose(gl);
    container.dispose(gl);
  }
  
  
  // ***************************************************
  /* INTERACTION
   * same as the lab exercise sheets
   *
   */
   
  private boolean animation = false;
  private double savedTime = 0;
   
  public void startAnimation() {
    animation = true;
    startTime = getSeconds()-savedTime;
  }
   
  public void stopAnimation() {
    animation = false;
    double elapsedTime = getSeconds()-startTime;
    savedTime = elapsedTime;
  }
   
  public void incXPosition() {
    xPosition += 0.5f;
    if (xPosition>5f) xPosition = 5f;
    updateMove();
  }
   
  public void decXPosition() {
    xPosition -= 0.5f;
    if (xPosition<-5f) xPosition = -5f;
    updateMove();
  }
 
  private void updateMove() {
    lampMoveTranslate.setTransform(Mat4Transform.translate(xPosition,0,0));
    lampMoveTranslate.update();
  }
  
 
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  private Camera camera;
  private Mat4 perspective;
   private Model floor, wall, window, tabletop, tableleg1, tableleg2, tableleg3, tableleg4, sphere, cube, cube2;
  private Model base1, lowerArm1, tail1, tail21, upperArm1, head1, nose1, ear1, ear21, book, container;
  private Light light;
  //private Light light1;
  private SGNode robotRoot;
  private SGNode lampRoot;
 
  private float xPosition = 0;
  private TransformNode translateX, lampMoveTranslate, lampTranslare, lowerArmRotate, upperArmRotate;
  
/*Define textures*/
  private void initialise(GL3 gl) {
    createRandomNumbers();
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
    int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
    int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/ear0xuu2.jpg");
    int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/cloud.jpg");
    int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/cloud2.jpg");
    int[] textureId9 = TextureLibrary.loadTexture(gl, "textures/wall.jpg");
    int[] textureId10 = TextureLibrary.loadTexture(gl, "textures/lampp.jpg");
    int[] textureId11 = TextureLibrary.loadTexture(gl, "textures/micey.jpg");
    int[] textureId12 = TextureLibrary.loadTexture(gl, "textures/cityscene.jpg");
        
    light = new Light(gl);
    light.setCamera(camera);
    

     

    /* draw floor */
    Mesh mesh = new Mesh (gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    floor = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3);


   /* Draw wall */
    mesh = new Mesh (gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply( Mat4Transform.rotateAroundX(90), modelMatrix );
    modelMatrix = Mat4.multiply( Mat4Transform.translate(0,16*0.5f,-16*0.5f), modelMatrix );
    wall = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId9);

    
    /* draw window */
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(new Vec3(2f, 2f, 2f), new Vec3(2f, 2f, 2f), new Vec3(2f, 2f, 2f), 32.0f);
    modelMatrix = Mat4Transform.scale(10f,7f,0.1f);
    //modelMatrix =  Mat4.multiply(Mat4Transform.rotateAroundX(90),modelMatrix);
    modelMatrix = Mat4.multiply( Mat4Transform.translate(0,18*0.5f,-7.9f), modelMatrix );
    window = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId12);


    /* draw tabletop */
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(10f,0.5f,4f), Mat4Transform.translate(0,9f,-1.5f));
    tabletop = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3);

   /* draw tablelegs */
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(1*0.3f,4.5f,0.3f), Mat4Transform.translate(-12f,0.5f,-24f));
    tableleg1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3); 

    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(1*0.3f,4.5f,0.3f), Mat4Transform.translate(12f,0.5f,-24f));
    tableleg2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3);

    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(1*0.3f,4.5f,0.3f), Mat4Transform.translate(-12f,0.5f,-16f));
    tableleg3 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3);

    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(1*0.3f,4.5f,0.3f), Mat4Transform.translate(12f,0.5f,-16f));
    tableleg4 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3);

   /* draw lamp base */
  NameNode base = new NameNode("base");
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(2.0f,0.5f,0.7f), Mat4Transform.translate(0,10f,-9f));
    base1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10);
      TransformNode baseTransform = new TransformNode("lowerArm transform", modelMatrix);
        ModelNode baseShape = new ModelNode("Sphere(lowerArm)", base1);
 
   /* draw lower arm */
   NameNode lowerArm = new NameNode("lowerArm");
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = new Mat4(1);
    modelMatrix = Mat4Transform.scale(0.3f,1f,0.3f);
    modelMatrix =  Mat4.multiply( Mat4Transform.rotateAroundZ(40), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-0.2f,5.6f,-6.3f), modelMatrix);
    lowerArm1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10);
      TransformNode lowerArmTransform = new TransformNode("lowerArm transform", modelMatrix);
        ModelNode lowerArmShape = new ModelNode("Sphere(lowerArm)", lowerArm1);
    
    /* draw tail */
   NameNode tail = new NameNode("tail"); 
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.4f,0.4f,0.4f), Mat4Transform.translate(-1.6f,15.3f,-15.7f));
    tail1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId2);
      TransformNode tailTransform = new TransformNode("tail transform", modelMatrix);
        ModelNode tailShape = new ModelNode("Sphere(tail)", tail1);

   /* draw tail2 */
   NameNode tail2 = new NameNode(""); 
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(0.5f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4Transform.scale(1f,0.1f,0.1f);
    modelMatrix =  Mat4.multiply( Mat4Transform.rotateAroundZ(-20), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-1.3f,6.35f,-6.3f), modelMatrix);
    tail21 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10);
     TransformNode tail2Transform = new TransformNode("tail2 transform", modelMatrix);
      ModelNode tail2Shape = new ModelNode("Sphere(tail2)", tail21);

   /* draw upperArm  */
   NameNode upperArm = new NameNode("upperArm"); 
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = new Mat4(1);
    modelMatrix = Mat4Transform.scale(0.3f,1.5f,0.3f);
    modelMatrix =  Mat4.multiply( Mat4Transform.rotateAroundZ(-20), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-0.33f,6.9f,-6.3f), modelMatrix);
    upperArm1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10);
      TransformNode upperArmTransform = new TransformNode("upperArm transform", modelMatrix);
        ModelNode upperArmShape = new ModelNode("Sphere(upperArm)", upperArm1);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
  
   /* draw head */
   NameNode head = new NameNode("head"); 
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = new Mat4(1);
    modelMatrix = Mat4Transform.scale(1f,0.36f,0.6f);
    modelMatrix =  Mat4.multiply( Mat4Transform.rotateAroundZ(-20), modelMatrix);
    modelMatrix = Mat4.multiply( Mat4Transform.translate(0f,7.7f,-6.3f), modelMatrix );
    head1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10);
     TransformNode headTransform = new TransformNode("head transform", modelMatrix);
       ModelNode headShape = new ModelNode("Cube(head)",head1);

   /* draw nose */
   NameNode nose = new NameNode("nose");
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = new Mat4(1);
    modelMatrix = Mat4Transform.scale(0.3f,0.18f,0.18f);
    modelMatrix =  Mat4.multiply( Mat4Transform.rotateAroundZ(-20), modelMatrix);
    modelMatrix = Mat4.multiply( Mat4Transform.translate(0.58f,7.4f,-6.3f), modelMatrix );
    nose1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10);
      TransformNode noseTransform = new TransformNode("nose transform",modelMatrix);
       ModelNode noseShape = new ModelNode("Cube(nose)", nose1);

   /* draw ear */
   NameNode ear = new NameNode("ear");
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = new Mat4(1);
    modelMatrix = Mat4Transform.scale(0.3f,0.3f,0.3f);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-0.5f,8.12f,-6f), modelMatrix);
    ear1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10);
      TransformNode earTransform = new TransformNode("ear transform", modelMatrix);
       ModelNode earShape = new ModelNode("Sphere(ear)", ear1);
   
  /* draw ear2 */
   NameNode ear2 = new NameNode("ear2");
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = new Mat4(1);
    modelMatrix = Mat4Transform.scale(0.3f,0.3f,0.3f);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-0.5f,8.12f,-6.5f), modelMatrix);
    ear21 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10);
     TransformNode ear2Transform = new TransformNode("ear1 transform", modelMatrix);
       ModelNode ear2Shape = new ModelNode("Sphere(ear2)", ear21);

  
   /* draw things on table */
    /* (1) sphere on the desk */
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.6f,0.6f,0.6f), Mat4Transform.translate(-5.3f,8.4f,-11.5f));
    sphere = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId8);
    /* (2)draw a cube */
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.5f,0.2f,1f), Mat4Transform.translate(-8f,24f,-5f));
    book = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId5);
    /* (3)pens container */
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.4f,0.4f,0.4f), Mat4Transform.translate(7f,12.45f,-13f));
    container = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId11);
    
    
    
   /*the lamp*/
    float legLength = 0.5f;
    lampRoot = new NameNode("lamp");
    lampMoveTranslate = new TransformNode("lamp transform",Mat4Transform.translate(xPosition,0,0));
    
    TransformNode lampTranslate = new TransformNode("lamp transform",Mat4Transform.translate(0,legLength,5));
    
   
   
    
     /* lamp add child */
    lampRoot.addChild(base);
     base.addChild(baseTransform);
      baseTransform.addChild(baseShape );
          baseShape.addChild(lowerArm);
                lowerArm.addChild(lowerArmTransform);
                   lowerArmTransform.addChild(lowerArmShape);
                    lowerArmShape.addChild(tail);
                       tail.addChild(tailTransform);
                        tailTransform.addChild(tailShape);
                            tailShape.addChild(tail2);
                               tail2.addChild(tail2Transform);
                                 tail2Transform.addChild(tail2Shape);
                                     tail2Shape.addChild(upperArm);
                                         upperArm.addChild(upperArmTransform);
                                          upperArmTransform.addChild(upperArmShape);
                                             upperArm.addChild(upperArmTransform);
                                              upperArmTransform.addChild(upperArmShape);
                                                    upperArmShape.addChild(head);  
                                                         head.addChild(headTransform);
                                                         headTransform.addChild(headShape);
                                                               headShape.addChild(nose);
                                                                   nose.addChild(noseTransform);
                                                                   noseTransform.addChild(noseShape);
                                                               headShape.addChild(ear);
                                                                   ear.addChild(earTransform);
                                                                   earTransform.addChild(earShape);
                                                               headShape.addChild(ear2);
                                                                   ear2.addChild(ear2Transform);
                                                                   ear2Transform.addChild(ear2Shape);
    

    
    
    lampRoot.update();  // IMPORTANT - don't forget this
    //robotRoot.print(0, false);
    //System.exit(0);
  }
  

  /* Draw whole things*/
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    light.setPosition(getLightPosition());  // changing light position each frame
    light.render(gl);
    floor.render(gl); 
    wall.render(gl);
    window.render(gl);
    tabletop.render(gl);
    tableleg1.render(gl);
    tableleg2.render(gl);
    tableleg3.render(gl);
    tableleg4.render(gl);
    base1.render(gl);
    lowerArm1.render(gl);
    tail1.render(gl);
    tail21.render(gl);
    upperArm1.render(gl);
    head1.render(gl);
    nose1.render(gl);
    ear1.render(gl);
    sphere.render(gl);
    book.render(gl);
    container.render(gl);
    ear21.render(gl);
   
  }

 
  
  // The light's postion is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition() {
    double elapsedTime = getSeconds()-startTime;
    float x = 6f*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
    float y = 1f;
    float z = 4.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)));
    return new Vec3(x,y,z);   
    //return new Vec3(5f,3.4f,5f);
  }

  
  // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  // ***************************************************
  /* An array of random numbers
   */ 
  
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
}