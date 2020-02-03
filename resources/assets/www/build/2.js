webpackJsonp([2],{

/***/ 696:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ProfilePageModule", function() { return ProfilePageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__profile__ = __webpack_require__(706);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var ProfilePageModule = /** @class */ (function () {
    function ProfilePageModule() {
    }
    ProfilePageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["I" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__profile__["a" /* ProfilePage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__profile__["a" /* ProfilePage */]),
            ],
        })
    ], ProfilePageModule);
    return ProfilePageModule;
}());

//# sourceMappingURL=profile.module.js.map

/***/ }),

/***/ 706:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ProfilePage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__providers_pict_pict__ = __webpack_require__(350);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__ionic_native_social_sharing__ = __webpack_require__(356);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__ionic_native_instagram__ = __webpack_require__(357);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};






/**
 * Generated class for the ProfilePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var ProfilePage = /** @class */ (function () {
    function ProfilePage(navCtrl, navParams, pictProvider, socialSharing, alertCtrl, instagram) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.pictProvider = pictProvider;
        this.socialSharing = socialSharing;
        this.alertCtrl = alertCtrl;
        this.instagram = instagram;
        this.message = null;
        this.file = null;
        this.link = null;
        this.subject = null;
    }
    ProfilePage.prototype.loginWithFB = function () {
        this.navCtrl.push("FacebookPage");
    };
    ProfilePage.prototype.doPrompt = function () {
        var _this = this;
        var prompt = this.alertCtrl.create({
            title: 'Share',
            message: "Enter the details of what you wish to share : ",
            inputs: [
                {
                    name: 'message',
                    placeholder: 'Enter the message :'
                },
                {
                    name: 'subject',
                    placeholder: 'Enter the subject : '
                },
                {
                    name: 'file',
                    placeholder: 'Enter the file :'
                },
                {
                    name: 'link',
                    placeholder: 'Enter the link :'
                },
            ],
            buttons: [
                {
                    text: 'Cancel',
                    handler: function (data) {
                        console.log('Cancel clicked');
                    }
                },
                {
                    text: 'Share',
                    handler: function (data) {
                        console.log(JSON.stringify(data));
                        console.log(data.message, data.subject, data.file, data.link);
                        _this.message = data.message;
                        _this.subject = data.subject;
                        _this.file = data.file;
                        _this.link = data.link;
                        _this.shareMedia();
                        console.log('Saved clicked');
                    }
                }
            ]
        });
        prompt.present();
    };
    ProfilePage.prototype.shareMedia = function () {
        this.socialSharing.share(this.message, this.subject, this.file, this.link)
            .then(function () {
        }).catch(function () {
        });
    };
    ProfilePage.prototype.shareInsta = function () {
        this.instagram.share(this.picture, 'This was copied to my Clipboard')
            .then(function () { return console.log('Shared!'); })
            .catch(function (error) { return console.error(error); });
    };
    ProfilePage.prototype.openCamera = function () {
        var _this = this;
        this.pictProvider.openCamera().then(function (value) {
            _this.picture = _this.pictProvider.myPhoto;
        }).catch(function (err) {
            console.log(err, "error in get picture from camera");
        });
    };
    ProfilePage.prototype.openGallery = function () {
        var _this = this;
        this.pictProvider.openGallery().then(function (value) {
            _this.picture = _this.pictProvider.myPhoto;
        }).catch(function (err) {
            console.log(err, "error in get picture from camera");
        });
    };
    ProfilePage.prototype.cropPicture = function () {
        var _this = this;
        this.pictProvider.cropPicture().then(function (value) {
            _this.picture = _this.pictProvider.myPhoto;
        }).catch(function (err) {
            console.log(err, "error in get picture from camera");
        });
    };
    ProfilePage.prototype.locationDirect = function () {
        this.navCtrl.setRoot("LocationPage");
    };
    ProfilePage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad ProfilePage');
    };
    ProfilePage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'page-profile',template:/*ion-inline-start:"D:\Ionic Project\Project\src\pages\profile\profile.html"*/'<!--\n  Generated template for the ProfilePage page.\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n  Ionic pages and navigation.\n-->\n<ion-header>\n\n  <ion-navbar>\n    <ion-title>Setup</ion-title>\n  </ion-navbar>\n\n</ion-header>\n\n\n<ion-content padding>\n\n    <ion-card>\n        <ion-card-content>\n          <ion-card-title>\n            Profile Setup\n            </ion-card-title>\n          <p>\n            Choose your profile picture using the Options Below : \n          </p>\n        </ion-card-content>\n      \n      <div text-center>\n      <ion-row no-padding>\n          <ion-col>\n              <button ion-button small color="blue" round (click)="openCamera()" icon-start>\n              <ion-icon name=\'camera\'></ion-icon>\n              Take the Picture\n            </button>\n          </ion-col>\n          <ion-col text-center>\n              <button ion-button small color="red" round (click)="openGallery()" icon-start>\n              <ion-icon name=\'image\'></ion-icon>\n              Picture from Gallery\n            </button>\n          </ion-col>\n          <ion-col >\n              <button ion-button small color="green" round (click)="cropPicture()" icon-start>\n              <ion-icon name=\'image\'></ion-icon>\n              Crop Picture\n            </button>\n          </ion-col>\n        </ion-row>\n      </div>\n    </ion-card>\n    <ion-card>    <p align="center"> \n      Below is the Picture :\n    </p> \n      <div>\n      <img src="{{ picture }}"/> \n      </div>\n      <br/>\n      <br/>\n      <br/>\n    </ion-card>\n    <div text-center>\n      <button ion-button color="secondary" round (click)="doPrompt()" icon-start>\n          <ion-icon name=\'share\'></ion-icon>\n          Share\n      </button><br/>\n      <button ion-button  color="blue" round (click)="locationDirect()" icon-start>\n          <ion-icon name=\'arrow-dropright-circle\'></ion-icon>Confirm</button><br/>\n      <button ion-button color="facebook" round (click)="loginWithFB()" icon-start>\n          <ion-icon name=\'logo-facebook\'></ion-icon> Login with Facebook  </button>\n      <button ion-button color="instagram" round (click)="shareInsta()" icon-start>\n          <ion-icon name=\'logo-instagram\'></ion-icon> Share on Instagram  </button>\n        </div>\n\n</ion-content>\n'/*ion-inline-end:"D:\Ionic Project\Project\src\pages\profile\profile.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["i" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* NavParams */], __WEBPACK_IMPORTED_MODULE_2__providers_pict_pict__["a" /* PictProvider */], __WEBPACK_IMPORTED_MODULE_3__ionic_native_social_sharing__["a" /* SocialSharing */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["a" /* AlertController */], __WEBPACK_IMPORTED_MODULE_4__ionic_native_instagram__["a" /* Instagram */]])
    ], ProfilePage);
    return ProfilePage;
}());

//# sourceMappingURL=profile.js.map

/***/ })

});
//# sourceMappingURL=2.js.map