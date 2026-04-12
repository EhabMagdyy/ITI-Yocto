# Building Recipe that compiles & installs an app in /usr/bin

## 0. source
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
