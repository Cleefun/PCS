@file:OptIn(ExperimentalUnsignedTypes::class)

package `fun`.clee.pcs.ip

import org.junit.Assert.*
import org.junit.Test

/**
 * Test class for [IpAddress6].
 *
 * @author Flente
 * @date 2023-03-05
 */
class IpAddress6Test {
    @Test
    fun createTest() {
        // Created by string address
        assertEquals("0:0:0:0:0:0:0:0", IpAddress6.create("::")!!.getHostAddress())
        assertEquals("0:0:0:0:0:0:0:1", IpAddress6.create("::1")!!.getHostAddress())
        assertEquals("1:0:0:0:0:0:0:0", IpAddress6.create("1::")!!.getHostAddress())
        // todo: host IP in this case should be invalid
        assertEquals("1:0:0:0:0:1:0:0", IpAddress6.create("1:0:0:0:0:1::")!!.getHostAddress())
        assertEquals("ffff:0:0:0:0:0:0:ffff", IpAddress6.create("ffff::ffff")!!.getHostAddress())
        assertEquals("ffff:1:0:0:0:0:0:ffff", IpAddress6.create("ffff:1::ffff")!!.getHostAddress())
        assertEquals("ffff:1:0:0:0:0:1:ffff", IpAddress6.create("ffff:1::1:ffff")!!.getHostAddress())
        assertEquals("ffff:1:1:0:0:0:1:ffff", IpAddress6.create("ffff:1:1::1:ffff")!!.getHostAddress())
        assertEquals("ffff:1:1:0:0:1:1:ffff", IpAddress6.create("ffff:1:1::1:1:ffff")!!.getHostAddress())
        assertEquals("ffff:1:1:1:0:1:1:ffff", IpAddress6.create("ffff:1:1:1:0:1:1:ffff")!!.getHostAddress())
        assertNull(IpAddress6.create("ffff:1:1:1::1:1:ffff"))
        assertNull(IpAddress6.create(":1:1:1:1:1:ffff"))
        assertNull(IpAddress6.create("10000::"))
        assertNull(IpAddress6.create("::-1"))
        assertNull(IpAddress6.create(":"))
        assertNull(IpAddress6.create(":::"))
        assertNull(IpAddress6.create("1::1::1"))
        assertNull(IpAddress6.create("0::"))
        assertNull(IpAddress6.create("::0"))
        assertNull(IpAddress6.create("::0"))
        // Created by number address
        UShortArray(8) { 0u }.apply { this[7] = 0xffffu }.run {
            assertEquals("0:0:0:0:0:0:0:ffff", IpAddress6.create(this)!!.getHostAddress())
        }
        assertNull(IpAddress6.create(UShortArray(7) { 0u }))
        assertNull(IpAddress6.create(UShortArray(9) { 0u }))
    }

    @Test
    fun isPublicAddressTest() {
        // any local address
        assertFalse(IpAddress6.create("::")!!.isPublicAddress())
        // loopback address
        assertFalse(IpAddress6.create("::1")!!.isPublicAddress())
        // site local address
        assertFalse(IpAddress6.create("fec0::")!!.isPublicAddress())
        assertFalse(IpAddress6.create("feff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")!!.isPublicAddress())
        // link local address
        assertFalse(IpAddress6.create("fe80::")!!.isPublicAddress())
        assertFalse(IpAddress6.create("febf:ffff:ffff:ffff:ffff:ffff:ffff:ffff")!!.isPublicAddress())
        // multicast address
        assertFalse(IpAddress6.create("ff00::")!!.isPublicAddress())
        assertFalse(IpAddress6.create("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")!!.isPublicAddress())
    }

    @Test
    fun isAnyLocalAddressTest() {
        assertTrue(IpAddress6.create("::")!!.isAnyLocalAddress())
    }

    @Test
    fun isLoopbackAddressTest() {
        assertTrue(IpAddress6.create("::1")!!.isLoopbackAddress())
    }

    @Test
    fun isSiteLocalAddressTest() {
        assertTrue(IpAddress6.create("fec0::")!!.isSiteLocalAddress())
        assertTrue(IpAddress6.create("feff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")!!.isSiteLocalAddress())
    }

    @Test
    fun isLinkLocalAddressTest() {
        assertTrue(IpAddress6.create("fe80::")!!.isLinkLocalAddress())
        assertTrue(IpAddress6.create("febf:ffff:ffff:ffff:ffff:ffff:ffff:ffff")!!.isLinkLocalAddress())
    }

    @Test
    fun isMulticastAddressTest() {
        assertTrue(IpAddress6.create("ff00::")!!.isMulticastAddress())
        assertTrue(IpAddress6.create("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff")!!.isMulticastAddress())
    }

    @Test
    fun equalsTest() {
        assertEquals(IpAddress6.create("::"), IpAddress6.create("0:0:0:0:0:0:0:0"))
        assertEquals(IpAddress6.create("::"), IpAddress6.create("0000:0000:0000:0000:0000:0000:0000:0000"))
        assertEquals(IpAddress6.create("::"), IpAddress6.create(UShortArray(8) { 0u }))
        assertNotEquals(IpAddress6.create("::"), IpAddress6.create("::1"))
    }
}