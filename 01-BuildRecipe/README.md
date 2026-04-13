# Building Recipe
<img width="915" height="508" alt="01-yocto_buildrecipe" src="https://github.com/user-attachments/assets/38087d3b-407a-44a8-b24c-cf96f609c0db" />

---

## 0. Source
``` bash
cd poky
source oe-init-build-env ../build-rpi/
```

## 1. Create Layer
 
```bash
bitbake-layers create-layer meta-weather
bitbake-layers add-layer meta-weather
```

---

## 2. Layer Structure

```
meta-weather/
├── conf
│   └── layer.conf
├── COPYING.MIT
├── README
└── recipes-apps
    └── sayhi
        ├── files
        │   └── 0001-fix-added-Ehab.patch   <== patch file (generated later)
        └── sayhi.bb                        <== your recipe
```

---

## 3. Licence checksum

``` bash
md5sum COPYING.MIT
# put it in recipe: 3da9cfbcb788c80a0384361b4de20420
```

---

## 4. Fetch

``` bash
bitbake sayhi -c clean
bitbake sayhi -c fetch
# if you make: bitbake sayhi -c compile without patch, you will get this error:
```
```
DEBUG: Executing shell function do_compile
main.c:3:1: warning: return type defaults to 'int' [-Wimplicit-int]
    3 | intmain() {
      | ^~~~~~~
/home/ehab/Documents/ITI_9Months/Yocto/shared-build/tmp/work/cortexa53-poky-linux/sayhi/1.0/recipe-sysroot-native/usr/bin/aarch64-poky-linux/../../libexec/aarch64-poky-linux/gcc/aarch64-poky-linux/13.4.0/ld: /home/ehab/Documents/ITI_9Months/Yocto/shared-build/tmp/work/cortexa53-poky-linux/sayhi/1.0/recipe-sysroot/usr/lib/Scrt1.o: in function `_start':
/usr/src/debug/glibc/2.39+git/csu/../sysdeps/aarch64/start.S:81:(.text+0x1c): undefined reference to `main'
/home/ehab/Documents/ITI_9Months/Yocto/shared-build/tmp/work/cortexa53-poky-linux/sayhi/1.0/recipe-sysroot-native/usr/bin/aarch64-poky-linux/../../libexec/aarch64-poky-linux/gcc/aarch64-poky-linux/13.4.0/ld: /usr/src/debug/glibc/2.39+git/csu/../sysdeps/aarch64/start.S:82:(.text+0x20): undefined reference to `main'
collect2: error: ld returned 1 exit status
WARNING: exit code 1 from a shell command.
```

---

## 5. Find your WORKDIR source (the repo we put in `SRC_URI`)
``` bash
ls ~/Documents/ITI_9Months/Yocto/shared-build/tmp/work/cortexa53-poky-linux/sayhi/1.0/git/
```

## 5. Create patch
``` bash
cd ~/Documents/ITI_9Months/Yocto/shared-build/tmp/work/cortexa53-poky-linux/sayhi/1.0/git/

# Initialize git so we can use format-patch
git init
git add main.c
git commit -m "original file"
git status
git add main.c
git commit -m "fix & added Ehab"
git add main.c
git format-patch -1 HEAD
cp 0001-fix-added-Ehab.patch ~/Documents/ITI_9Months/Yocto/meta-weather/recipes-apps/sayhi/files/
```

---

## 6. Apply patch & compile

``` bash
bitbake sayhi -c patch
bitbake sayhi -c compile
```

---
 
## 7. Generate the package
 
```bash
# Run install + package
bitbake sayhi
# Verify the binary is in the package
oe-pkgdata-util list-pkg-files sayhi
# expected:
```
```
sayhi:
	/usr/bin/ehab
```

---

## 8. To Get It Into the Image
### A) Tell the image to include it (local.conf)
``` bash
nano ~/Documents/ITI_9Months/Yocto/build-rpi/conf/local.conf
```

### Add this line at the bottom:
```
# append the package
IMAGE_INSTALL:append = " sayhi"
```
### B) Build the full image
``` bash
bashbitbake core-image-minimal -k
```
