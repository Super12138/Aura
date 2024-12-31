package com.kyant.m3color.hct

import com.kyant.aura.core.hct.Cam16Ucs
import com.kyant.aura.core.hct.DefaultViewingConditions
import org.junit.Test
import kotlin.test.assertEquals

class Cam16Tests {
    @Test
    fun defaultCondValues() {
        val vc = ViewingConditions.DEFAULT
        val d = DefaultViewingConditions

        assertEquals(vc.aw, d.aw)
        assertEquals(vc.nbb, d.nbb)
        assertEquals(vc.ncb, d.ncb)
        assertEquals(vc.c, d.c)
        assertEquals(vc.nc, 1.0)
        assertEquals(vc.flRoot, d.flRoot)
        assertEquals(vc.z, d.z)
    }

    @Test
    fun cam16() {
        run {
            val c = Cam16.fromInt(0x6200EE)
            val c2 = com.kyant.aura.core.hct.Cam16.fromInt(0x6200EE)
            assertEquals(c, c2)
        }

        run {
            val c = Cam16.fromInt(0xFFFFFF)
            val c2 = com.kyant.aura.core.hct.Cam16.fromInt(0xFFFFFF)
            tryAssertEquals(c.j, c2.j)
            tryAssertEquals(c.chroma, c2.chroma)
            tryAssertEquals(c.hue, c2.hue)
            assertEquals(c, c2)
        }

        run {
            val c = Cam16.fromInt(0x000000)
            val c2 = com.kyant.aura.core.hct.Cam16.fromInt(0x000000)
            assertEquals(c, c2)
        }

        run {
            val c = Cam16.fromJch(40.0, 100.0, 270.0)
            val c2 = com.kyant.aura.core.hct.Cam16.fromJch(40.0, 100.0, 270.0)
            assertEquals(c, c2)
        }

        run {
            val c = Cam16.fromJch(40.0, 0.0, 270.0)
            val c2 = com.kyant.aura.core.hct.Cam16.fromJch(40.0, 0.0, 270.0)
            assertEquals(c, c2)
        }

        run {
            val c = Cam16.fromJch(0.0, 0.0, 270.0)
            val c2 = com.kyant.aura.core.hct.Cam16.fromJch(0.0, 0.0, 270.0)
            assertEquals(c, c2)
        }

        run {
            val c = Cam16.fromUcs(0.0, 0.0, 0.0)
            val c2 = com.kyant.aura.core.hct.Cam16.fromUcs(0.0, 0.0, 0.0)
            assertEquals(c, c2)
        }

        run {
            val c = Cam16.fromUcs(50.0, 10.0, 10.0)
            val c2 = com.kyant.aura.core.hct.Cam16.fromUcs(50.0, 10.0, 10.0)
            assertEquals(c, c2)
        }

        run {
            val c = Cam16.fromJch(50.0, 0.0, 270.0)
            val c2 = com.kyant.aura.core.hct.Cam16.fromJch(50.0, 0.0, 270.0)
            assertEquals(c, c2)
        }
    }

    private fun assertEquals(a: Cam16, b: com.kyant.aura.core.hct.Cam16) {
        tryAssertEquals(a.hue, b.hue)
        tryAssertEquals(a.chroma, b.chroma)
        tryAssertEquals(a.j, b.j)

        assertEquals(a.toInt(), com.kyant.aura.core.hct.Cam16.toInt(b.j, b.chroma, b.hue))

        val c = Cam16Ucs.fromCam16(b)
        tryAssertEquals(a.jstar, c.jstar)
        tryAssertEquals(a.astar, c.astar)
        tryAssertEquals(a.bstar, c.bstar)

        assertEquals(
            c.distance(Cam16Ucs.fromCam16(com.kyant.aura.core.hct.Cam16.fromUcs(c.jstar, c.astar, c.bstar))),
            0.0,
            1E-8
        )
    }

    private fun tryAssertEquals(a: Double, b: Double) {
        try {
            assertEquals(a, b)
        } catch (e: AssertionError) {
            // println(e.message)
            assertEquals(a, b, 1E-11)
        }
    }
}
