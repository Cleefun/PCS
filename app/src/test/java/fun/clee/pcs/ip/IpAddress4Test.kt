@file:OptIn(ExperimentalUnsignedTypes::class)

package `fun`.clee.pcs.ip

import org.junit.Assert.*
import org.junit.Test

/**
 * Test class for [IpAddress4].
 *
 * @author Flente
 * @date 2023-03-05
 */
class IpAddress4Test {
    @Test
    fun createTest() {
        // Created by string address
        assertEquals("255.0.0.1", IpAddress4.create("255.0.0.1")!!.getHostAddress())
        assertEquals("255.0.0.1", IpAddress4.create("255.000.000.001")!!.getHostAddress())
        assertNull(IpAddress4.create("256.0.0.1"))
        assertNull(IpAddress4.create("255.0.0.-1"))
        assertNull(IpAddress4.create("255.0.0."))
        assertNull(IpAddress4.create(".255.0.0"))
        assertNull(IpAddress4.create("255.0.0.0."))
        // Created by number address
        assertEquals("255.0.0.1", IpAddress4.create(listOf<UByte>(255u, 0u, 0u, 1u).toUByteArray())!!.getHostAddress())
        assertNull(IpAddress4.create(listOf<UByte>(255u, 0u, 0u).toUByteArray()))
        assertNull(IpAddress4.create(listOf<UByte>(255u, 0u, 0u, 1u, 1u).toUByteArray()))
    }

    @Test
    fun isPublicAddressTest() {
        // any local address
        assertFalse(IpAddress4.create("0.0.0.0")!!.isPublicAddress())
        // loopback address
        assertFalse(IpAddress4.create("127.0.0.0")!!.isPublicAddress())
        assertFalse(IpAddress4.create("127.255.255.255")!!.isPublicAddress())
        // site local address
        assertFalse(IpAddress4.create("10.0.0.0")!!.isPublicAddress())
        assertFalse(IpAddress4.create("10.255.255.255")!!.isPublicAddress())
        assertFalse(IpAddress4.create("172.16.0.0")!!.isPublicAddress())
        assertFalse(IpAddress4.create("172.31.255.255")!!.isPublicAddress())
        assertFalse(IpAddress4.create("192.168.0.0")!!.isPublicAddress())
        assertFalse(IpAddress4.create("192.168.255.255")!!.isPublicAddress())
        // link local address
        assertFalse(IpAddress4.create("169.254.0.0")!!.isPublicAddress())
        assertFalse(IpAddress4.create("169.254.255.255")!!.isPublicAddress())
        // multicast address
        assertFalse(IpAddress4.create("224.0.0.0")!!.isPublicAddress())
        assertFalse(IpAddress4.create("239.255.255.255")!!.isPublicAddress())
    }

    @Test
    fun isAnyLocalAddressTest() {
        assertTrue(IpAddress4.create("0.0.0.0")!!.isAnyLocalAddress())
    }

    @Test
    fun isLoopbackAddressTest() {
        assertTrue(IpAddress4.create("127.0.0.0")!!.isLoopbackAddress())
        assertTrue(IpAddress4.create("127.255.255.255")!!.isLoopbackAddress())
    }

    @Test
    fun isSiteLocalAddressTest() {
        assertTrue(IpAddress4.create("10.0.0.0")!!.isSiteLocalAddress())
        assertTrue(IpAddress4.create("10.255.255.255")!!.isSiteLocalAddress())
        assertTrue(IpAddress4.create("172.16.0.0")!!.isSiteLocalAddress())
        assertTrue(IpAddress4.create("172.31.255.255")!!.isSiteLocalAddress())
        assertTrue(IpAddress4.create("192.168.0.0")!!.isSiteLocalAddress())
        assertTrue(IpAddress4.create("192.168.255.255")!!.isSiteLocalAddress())
    }

    @Test
    fun isLinkLocalAddressTest() {
        assertTrue(IpAddress4.create("169.254.0.0")!!.isLinkLocalAddress())
        assertTrue(IpAddress4.create("169.254.255.255")!!.isLinkLocalAddress())
    }

    @Test
    fun isMulticastAddressTest() {
        assertTrue(IpAddress4.create("224.0.0.0")!!.isMulticastAddress())
        assertTrue(IpAddress4.create("239.255.255.255")!!.isMulticastAddress())
    }

    @Test
    fun equalsTest() {
        assertEquals(IpAddress4.create("255.0.0.1"), IpAddress4.create("255.0.0.1"))
        assertEquals(IpAddress4.create("255.000.000.001"), IpAddress4.create("255.0.0.1"))
        assertEquals(IpAddress4.create("255.0.0.1"), IpAddress4.create(listOf<UByte>(255u, 0u, 0u, 1u).toUByteArray()))
        assertNotEquals(IpAddress4.create("255.0.0.1"), IpAddress4.create("255.0.0.0"))
    }
}