SUMMARY = "Static IP and WiFi configuration"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
    file://10-static-wifi.network \
    file://wpa_supplicant-wlan0.conf \
"

S = "${WORKDIR}"

# Inherit systemd to get access to ${systemd_unitdir} paths
inherit systemd

RDEPENDS:${PN} = "wpa-supplicant"

do_install() {
    # 1. Install Network config
    # ${systemd_unitdir} automatically handles /lib vs /usr/lib based on usrmerge
    install -d ${D}${systemd_unitdir}/network
    install -m 0644 ${WORKDIR}/10-static-wifi.network ${D}${systemd_unitdir}/network/

    # 2. Install WPA Supplicant config
    # /etc remains /etc even with usrmerge
    install -d ${D}${sysconfdir}/wpa_supplicant
    install -m 0600 ${WORKDIR}/wpa_supplicant-wlan0.conf ${D}${sysconfdir}/wpa_supplicant/

    # 3. Manually enable the service
    # We point the symlink to the absolute path where the service WILL be on the target
    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
    ln -sf ${systemd_unitdir}/system/wpa_supplicant@.service \
           ${D}${sysconfdir}/systemd/system/multi-user.target.wants/wpa_supplicant@wlan0.service
}

# Use broad wildcards to ensure BitBake finds the files regardless of the internal path shifts
FILES:${PN} += " \
    ${systemd_unitdir}/network/*.network \
    ${sysconfdir}/wpa_supplicant/wpa_supplicant-wlan0.conf \
    ${sysconfdir}/systemd/system/multi-user.target.wants/wpa_supplicant@wlan0.service \
"
