webpackJsonp([3],{

/***/ 695:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MainPageModule", function() { return MainPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__main__ = __webpack_require__(705);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var MainPageModule = /** @class */ (function () {
    function MainPageModule() {
    }
    MainPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["I" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__main__["a" /* MainPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__main__["a" /* MainPage */]),
            ],
        })
    ], MainPageModule);
    return MainPageModule;
}());

//# sourceMappingURL=main.module.js.map

/***/ }),

/***/ 705:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return MainPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
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
 * Generated class for the MainPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var MainPage = /** @class */ (function () {
    function MainPage(navCtrl, navParams, menu) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.menu = menu;
        this.tab1Root = "InfoPage";
        this.tab2Root = "DetailsPage";
        this.menu1Active();
    }
    MainPage.prototype.menu1Active = function () {
        this.activeMenu = 'menu1';
        this.menu.enable(true, 'menu1');
        this.menu.enable(false, 'menu2');
    };
    MainPage.prototype.menu2Active = function () {
        this.activeMenu = 'menu2';
        this.menu.enable(false, 'menu1');
        this.menu.enable(true, 'menu2');
    };
    MainPage.prototype.infiniteDirect = function () {
        this.navCtrl.push("InfoPage");
    };
    MainPage.prototype.sqlDirect = function () {
        this.navCtrl.push("SqlPage");
    };
    MainPage.prototype.virtualDirect = function () {
        this.navCtrl.push("VirinfoPage");
    };
    MainPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad MainPage');
    };
    MainPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'page-main',
            template: '<ion-menu [content]="content" id="menu1"> <ion-header> <ion-toolbar color="secondary"> <ion-title>Menu 1</ion-title> </ion-toolbar> </ion-header><ion-content><ion-list><button ion-item menuClose="menu1" detail-none>Close Menu 1</button></ion-list></ion-content></ion-menu><ion-menu [content]="content" id="menu2"><ion-header><ion-toolbar color="danger"><ion-title>Menu 2</ion-title></ion-toolbar></ion-header><ion-content><ion-list><button ion-item menuClose="menu2" detail-none>Close Menu 2</button></ion-list></ion-content></ion-menu><ion-nav [root]="rootPage" #content swipeBackEnabled="false"></ion-nav>',template:/*ion-inline-start:"D:\Ionic Project\Project\src\pages\main\main.html"*/'<ion-header>\n\n    <ion-navbar>\n      <button ion-button [menuToggle]="activeMenu">\n        <ion-icon name="menu"></ion-icon>\n      </button>\n      <ion-title>\n        Main Page\n      </ion-title>\n    </ion-navbar>\n  \n  </ion-header>\n  <ion-menu [content]="content" id="menu1">\n\n      <ion-header>\n        <ion-toolbar color="secondary">\n          <ion-title>Menu 1</ion-title>\n        </ion-toolbar>\n      </ion-header>\n    \n      <ion-content>\n        <ion-list>\n          <button ion-item menuClose="menu1" detail-none (click)="sqlDirect()">\n            SQL DEMO\n          </button>\n        </ion-list>\n      </ion-content>\n    \n    </ion-menu>\n    \n    \n    <ion-menu [content]="content" id="menu2">\n    \n      <ion-header>\n        <ion-toolbar color="danger">\n          <ion-title>Menu 2</ion-title>\n        </ion-toolbar>\n      </ion-header>\n    \n      <ion-content>\n        <ion-list>\n          <button ion-item menuClose="menu2" detail-none (click)="infiniteDirect()">\n            Infinite Scroll\n          </button>\n          <button ion-item menuClose="menu2" detail-none (click)="virtualDirect()">\n            Virtual Scroll\n          </button>\n        </ion-list>\n      </ion-content>\n    \n    </ion-menu>\n    \n    <ion-nav [root]="rootPage" #content swipeBackEnabled="false"></ion-nav>\n    <ion-tabs tabs-only>\n        <ion-tab [root]="tab1Root"></ion-tab>\n        <ion-tab [root]="tab2Root"></ion-tab>\n      </ion-tabs>\n    \n\n  <ion-content padding>\n  \n    <h4>Choose the Menu : </h4>\n    <p> Currently Active Menu is : \n    <b color="primary">{{ (activeMenu == \'menu1\') ? \'Menu 1\' : \'Menu 2\' }}</b>\n    </p>\n    <button ion-button block color="secondary" (click)="menu1Active()">Make Menu 1 Active</button>\n  \n    <button ion-button block color="danger" (click)="menu2Active()">Make Menu 2 Active</button>\n  \n    <button ion-button block [menuToggle]="activeMenu">Toggle Menu</button>\n\n    \n  </ion-content>'/*ion-inline-end:"D:\Ionic Project\Project\src\pages\main\main.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["i" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* NavParams */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["h" /* MenuController */]])
    ], MainPage);
    return MainPage;
}());

//# sourceMappingURL=main.js.map

/***/ })

});
//# sourceMappingURL=3.js.map