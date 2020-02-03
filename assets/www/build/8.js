webpackJsonp([8],{

/***/ 690:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "FacebookPageModule", function() { return FacebookPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__facebook__ = __webpack_require__(700);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var FacebookPageModule = /** @class */ (function () {
    function FacebookPageModule() {
    }
    FacebookPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["I" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__facebook__["a" /* FacebookPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__facebook__["a" /* FacebookPage */]),
            ],
        })
    ], FacebookPageModule);
    return FacebookPageModule;
}());

//# sourceMappingURL=facebook.module.js.map

/***/ }),

/***/ 700:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return FacebookPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ionic_native_facebook__ = __webpack_require__(352);
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
 * Generated class for the FacebookPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var FacebookPage = /** @class */ (function () {
    function FacebookPage(navCtrl, navParams, facebook) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.facebook = facebook;
    }
    FacebookPage.prototype.loginWithFB = function () {
        var _this = this;
        this.facebook.login(['email', 'public_profile']).then(function (response) {
            _this.facebook.api('me?fields=id,name,email,first_name,picture.width(720).height(720).as(picture_large)', []).then(function (profile) {
                _this.userData = { email: profile['email'], first_name: profile['first_name'], picture: profile['picture_large']['data']['url'], username: profile['name'] };
                _this.pict = _this.userData.picture;
            });
        });
    };
    FacebookPage.prototype.locationDirect = function () {
        this.navCtrl.setRoot("LocationPage");
    };
    FacebookPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad FacebookPage');
    };
    FacebookPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'page-facebook',template:/*ion-inline-start:"D:\Ionic Project\Project\src\pages\facebook\facebook.html"*/'<!--\n  Generated template for the FacebookPage page.\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n  Ionic pages and navigation.\n-->\n<ion-header>\n\n  <ion-navbar>\n    <ion-title>Facebook</ion-title>\n  </ion-navbar>\n\n</ion-header>\n\n\n<ion-content padding>\n\n<button ion-button color="facebook" round (click)="loginWithFB()">Login with Facebook</button>\n    <ion-card *ngIf="userData">\n        <ion-card-header>{{ userData.username }}</ion-card-header>\n        <img [src]="userData.picture" />\n        <ion-card-content>\n          <p>Email: {{ userData.email }}</p>\n          <p>First Name: {{ userData.first_name }}</p>\n          <img [src]="pict" />\n        </ion-card-content>\n      </ion-card>\n<button ion-button  color="blue" round (click)="locationDirect()">Continue -></button>\n\n</ion-content>\n'/*ion-inline-end:"D:\Ionic Project\Project\src\pages\facebook\facebook.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["i" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* NavParams */], __WEBPACK_IMPORTED_MODULE_2__ionic_native_facebook__["a" /* Facebook */]])
    ], FacebookPage);
    return FacebookPage;
}());

//# sourceMappingURL=facebook.js.map

/***/ })

});
//# sourceMappingURL=8.js.map