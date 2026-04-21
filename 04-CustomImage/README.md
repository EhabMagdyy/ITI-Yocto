# Custom Yocto Image for Raspberry Pi 3 (64-bit) with
<img width="1287" height="991" alt="yocto-04-customimage" src="https://github.com/user-attachments/assets/bb08b608-925f-4f3f-a85b-ac7c22312183" />

---

## Overview

This guide documents the steps to create a custom Yocto image (`core-image-custom`) for the Raspberry Pi 3 64-bit, using the `poky` distro with `systemd` as the init manager and NFS as the root filesystem.

---

## 1. Prerequisites

A working Yocto build environment with `poky` and `meta-raspberrypi` already set up and `core-image-minimal` building successfully.

---

## 2. Clone Additional Layers

`systemd` requires `meta-openembedded` layers:

```sh
cd /home/ehab/Documents/ITI_9Months/Yocto
git clone git@github.com:openembedded/meta-openembedded.git -b scarthgap
```

Add the layers to your build:

```sh
bitbake-layers add-layer meta-openembedded/meta-oe
bitbake-layers add-layer meta-openembedded/meta-python
bitbake-layers add-layer meta-openembedded/meta-networking
```

---

## 3. Create the Custom Layer

```bash
bitbake-layers create-layer -a meta-customimage
mkdir -p meta-customimage/recipes-core/images
touch meta-customimage/recipes-core/images/core-image-custom.bb
```

---

## 4. Create the Image Recipe

**`meta-customimage/recipes-core/images/core-image-custom.bb`:**

```sh
SUMMARY = "My Custom Yocto Image"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit core-image

IMAGE_FEATURES:append = " ssh-server-dropbear debug-tweaks"
IMAGE_INSTALL:append = " python3 tcpdump"

# Minimum 12MB
IMAGE_ROOTFS_SIZE ?= "12288"
# Add 50% overhead for metadata
IMAGE_OVERHEAD_FACTOR = "1.5"
IMAGE_FSTYPES = "ext3 tar.bz2 wic.bz2"
```

```
normal recipe  →  produces an RPM package
image recipe   →  inherits core-image → produces a rootfs + image files
```
#### core-image.bbclass does several things that make a recipe behave as an image:

- It defines do_rootfs — the task that assembles the root filesystem by installing packages via DNF/RPM
- It defines do_image — the task that produces the final image files (.tar.bz2, .ext3, etc.)
- It defines do_image_complete — post-processing
- It sets IMAGE_INSTALL with packagegroup-core-boot as the base
- It makes IMAGE_FEATURES, IMAGE_INSTALL, IMAGE_FSTYPES meaningful


> **Note:** Do not set `DISTRO_FEATURES` or `VIRTUAL-RUNTIME_*` in the image recipe — these must go in `local.conf` only.

---

## 5. Configure local.conf

Add the following to `build-rpi/conf/local.conf`:

```sh
# systemd as init manager
DISTRO_FEATURES:append = " systemd usrmerge"
VIRTUAL-RUNTIME_init_manager = "systemd"
LICENSE_FLAGS_ACCEPTED = "synaptics-killswitch"

# Build performance
BB_NUMBER_THREADS = "12"

# Serial console
SERIAL_CONSOLES = "115200;ttyAMA0"
RPI_EXTRA_CONFIG = "dtoverlay=miniuart-bt"

# NFS root
CMDLINE_ROOT_PARTITION = "/dev/nfs nfsroot=192.168.2.1:/srv/nfs/rootfs,v3,tcp ip=192.168.2.2:192.168.2.1::255.255.255.0::eth0:off"
```

### Why these settings

| Variable | Reason |
|---|---|
| `systemd usrmerge` in `DISTRO_FEATURES` | systemd in scarthgap requires `usrmerge` (merged `/usr`) |
| `sysvinit` removed | Prevents sysvinit packages from being pulled in alongside systemd |
| `VIRTUAL-RUNTIME_*` | Tells Yocto which runtime providers to use for init, login, udev |
| `LICENSE_FLAGS_ACCEPTED` | RPi WiFi/BT firmware has a restricted license that must be explicitly accepted |

---

## 6. Build the Image

```bash
bitbake core-image-custom
```

Output images will be in:
```
shared-build/tmp/deploy/images/raspberrypi3-64/
```

---

## 7. Deploy to NFS

```bash
sudo mkdir -p /srv/nfs/rootfs
sudo tar -xjf shared-build/tmp/deploy/images/raspberrypi3-64/core-image-custom-raspberrypi3-64.rootfs.tar.bz2 \
    -C /srv/nfs/rootfs
```

Verify systemd is present:
```bash
ls /srv/nfs/rootfs/etc/systemd/
```

---

## 8. Boot the RPi

The kernel command line configured in `local.conf` tells the RPi to mount the rootfs over NFS at boot. No SD card rootfs is needed — only the kernel and bootloader files are required on the SD card.

After boot, verify systemd is running:
```bash
systemctl list-units --type=service
```
