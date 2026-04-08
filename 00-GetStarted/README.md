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
SERIAL_CONSOLES = "115200;ttyAMA0"
# Build
bitbake core-image-minimal -k
```

### 5. Decompress and Flash
``` bash
cd shared-build/tmp/deploy/images/raspberrypi3-64/
bzip2 -df core-image-minimal-raspberrypi3-64.rootfs.wic.bz2

# Unmount everything on the disk first
sudo umount /dev/sdb1 2>/dev/null
sudo umount /dev/sdb2 2>/dev/null

# Wipe the partition table and first sectors completely
sudo wipefs -a /dev/sdb

# Zero out the first 100MB to be sure
sudo dd if=/dev/zero of=/dev/sdb bs=1M count=100 conv=fsync

# Confirm disk is clean
sudo fdisk -l /dev/sdb

# Flash image
sudo dd if=core-image-minimal-raspberrypi3-64.rootfs.wic of=/dev/sdb bs=4M status=progress conv=fsync
``` 

### using bmap tool to load compressed image
``` bash
# Faster - Decompresses on the fly - Verifies checksums - Skips empty blocks
sudo apt install bmap-tools
sudo bmaptool copy core-image-minimal-raspberrypi3-64.rootfs-20260404214557.wic.bz2 /dev/sdb

# Just make sure the .bmap file is in the same directory, bmaptool picks it up automatically.
```

### 6. Work with USB-TTL
``` bash
# in config.txt
dtoverlay=miniuart-bt

# cmdline.txt
dwc_otg.lpm_enable=0 root=/dev/sda2 rootwait console=tty1 console=ttyAMA0,115200 rootfstype=ext4 net.ifnames=0
```
---

## Notes

- YOCTO needs absloute path not relative path
- in this example, i'm using USB Disk not SD Card
