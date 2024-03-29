package com.ccooy.apidemos.graphics

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

import javax.microedition.khronos.opengles.GL10

/**
 * A vertex shaded cube.
 */
internal class Cube {

    private val mVertexBuffer: IntBuffer
    private val mColorBuffer: IntBuffer
    private val mIndexBuffer: ByteBuffer

    init {
        val one = 0x10000
        val vertices = intArrayOf(
            -one, -one, -one,
            one, -one, -one,
            one, one, -one,
            -one, one, -one,
            -one, -one, one,
            one, -one, one,
            one, one, one,
            -one, one, one
        )

        val colors = intArrayOf(
            0, 0, 0, one,
            one, 0, 0, one,
            one, one, 0, one,
            0, one, 0, one,
            0, 0, one, one,
            one, 0, one, one,
            one, one, one, one,
            0, one, one, one
        )

        val indices = byteArrayOf(
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0,
            4, 7, 6, 4, 6, 5,
            3, 0, 1, 3, 1, 2
        )

        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder())
        mVertexBuffer = vbb.asIntBuffer()
        mVertexBuffer.put(vertices)
        mVertexBuffer.position(0)

        val cbb = ByteBuffer.allocateDirect(colors.size * 4)
        cbb.order(ByteOrder.nativeOrder())
        mColorBuffer = cbb.asIntBuffer()
        mColorBuffer.put(colors)
        mColorBuffer.position(0)

        mIndexBuffer = ByteBuffer.allocateDirect(indices.size)
        mIndexBuffer.put(indices)
        mIndexBuffer.position(0)
    }

    fun draw(gl: GL10) {
        gl.glFrontFace(GL10.GL_CW)
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer)
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer)
        gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer)
    }
}
