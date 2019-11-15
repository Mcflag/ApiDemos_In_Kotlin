package com.ccooy.apidemos.graphics

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLSurfaceView

/**
 * Render a pair of tumbling cubes.
 */

class CubeRenderer(private val mTranslucentBackground: Boolean) : GLSurfaceView.Renderer {
    private val mCube: Cube = Cube()
    private var mAngle: Float = 0.toFloat()

    override fun onDrawFrame(gl: GL10) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)

        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
        gl.glTranslatef(0f, 0f, -3.0f)
        gl.glRotatef(mAngle, 0f, 1f, 0f)
        gl.glRotatef(mAngle * 0.25f, 1f, 0f, 0f)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY)

        mCube.draw(gl)

        gl.glRotatef(mAngle * 2.0f, 0f, 1f, 1f)
        gl.glTranslatef(0.5f, 0.5f, 0.5f)

        mCube.draw(gl)

        mAngle += 1.2f
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()
        gl.glFrustumf(-ratio, ratio, -1f, 1f, 1f, 10f)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        gl.glDisable(GL10.GL_DITHER)

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)

        if (mTranslucentBackground) {
            gl.glClearColor(0f, 0f, 0f, 0f)
        } else {
            gl.glClearColor(1f, 1f, 1f, 1f)
        }
        gl.glEnable(GL10.GL_CULL_FACE)
        gl.glShadeModel(GL10.GL_SMOOTH)
        gl.glEnable(GL10.GL_DEPTH_TEST)
    }
}
