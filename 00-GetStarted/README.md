# YOCTO

## Get Started

### 1. Create a directory to contain poky & meta & build & shared build
``` bash
mkdir Yocto
```
### 2. Clone poky
``` bash
git clone git@github.com:yoctoproject/poky.git
cd poky
git checkout scarthgap
```
### 3. Clone meta RPi from open embedded store
``` bash
git clone git@github.com:agherzan/meta-raspberrypi.git
cd meta-raspberrypi
git checkout scarthgap
```
### 4. Setup environment & Configure image & Run

``` bash
cd poky/
source oe-init-build-env ../build-rpi

# Now local.conf & bblayers.conf are generated under build-rpi/conf

# Configure bblayers.conf
# add meta-raspberrypi
bitbake add-layers /home/ehab/Documents/ITI_9Months/Yocto/meta-raspberrypi

# create a meta & add it to bblayers.conf
bitbake-layers create-layer /home/ehab/Documents/ITI_9Months/Yocto/meta-weather
bitbake-layers add-layer /home/ehab/Documents/ITI_9Months/Yocto/meta-weather

# Configure local.conf
# Set your machine
# we get it from: meta-raspberrypi/conf/machine/
MACHINE="raspberrypi3-64"
# Create a shared folder under Yocto for build
mkdir shared-build
# in local.conf
DL_DIR ?= "/home/ehab/Documents/ITI_9Months/Yocto/shared-build/downloads"
SSTATE_DIR ?= "/home/ehab/Documents/ITI_9Months/Yocto/shared-build/sstate-cache"
TMPDIR = "/home/ehab/Documents/ITI_9Months/Yocto/shared-build/tmp"
# Select number of threads for build
BB_NUMBER_THREADS = "12"
# Enable console on serial port - to show login shell after booting
# We are telling Yocto: "Please run a login shell on /dev/ttyAMA0 at a baud rate of 115200."
SERIAL_CONSOLES = "115200;ttyAMA0"
RPI_EXTRA_CONFIG = "dtoverlay=miniuart-bt"
CMDLINE_ROOT_PARTITION = "/dev/sda2"

# Build
bitbake core-image-minimal -k
```

---

### 5. Check your bootfile & rootfs

```bash
# the generated image's boot partition
ls ~/Documents/ITI_9Months/Yocto/shared-build/tmp/deploy/images/raspberrypi3-64/bootfiles/

# the generated image's root partition
ls ~/Documents/ITI_9Months/Yocto/shared-build/tmp/work/raspberrypi3_64-poky-linux/core-image-minimal/1.0/rootfs/
```

---

### 6. Clear your USB Disk & Flash
``` bash
# Unmount everything on the disk first
sudo umount /dev/sdb1 2>/dev/null
sudo umount /dev/sdb2 2>/dev/null

# Wipe the partition table and first sectors completely
sudo wipefs -a /dev/sdb

# Zero out the first 100MB to be sure
sudo dd if=/dev/zero of=/dev/sdb bs=1M count=100 conv=fsync

# Confirm disk is clean
sudo fdisk -l /dev/sdb

# Flash image using bmap tool
# Faster - Decompresses on the fly - Verifies checksums - Skips empty blocks
cd ~/Documents/ITI_9Months/Yocto/shared-build/tmp/deploy/images/raspberrypi3-64
sudo apt install bmap-tools
sudo bmaptool copy core-image-minimal-raspberrypi3-64.rootfs-xxxxxxxxxx.wic.bz2 /dev/sdb
# Just make sure the .bmap file is in the same directory, bmaptool picks it up automatically.

# eject & remove your usb disk
sudo eject /dev/sdb
``` 

### 7. Work with USB-TTL
``` bash
# in config.txt put
dtoverlay=miniuart-bt
# instead of:
# enable_uart=1
# it doesn't work with me

# This Device Tree Overlay (dtoverlay=miniuart-bt) swaps the UARTs.

# It moves the Mini UART to the Bluetooth port (Bluetooth is low-speed and doesn't care about the clock fluctuations).
# It moves the PL011 UART (the good one) to the GPIO pins 14 and 15.
# Since the PL011 has its own dedicated clock, the data remains stable regardless of CPU speed changes.
```
---

## Notes

- YOCTO needs absloute path not relative path
- in this example, i'm using USB Disk not SD Card
