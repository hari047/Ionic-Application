webpackJsonp([1],{

/***/ 697:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "SqlPageModule", function() { return SqlPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__sql__ = __webpack_require__(707);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var SqlPageModule = /** @class */ (function () {
    function SqlPageModule() {
    }
    SqlPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["I" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__sql__["a" /* SqlPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__sql__["a" /* SqlPage */]),
            ],
        })
    ], SqlPageModule);
    return SqlPageModule;
}());

//# sourceMappingURL=sql.module.js.map

/***/ }),

/***/ 707:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SqlPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__providers_database_database__ = __webpack_require__(358);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var SqlPage = /** @class */ (function () {
    function SqlPage(navCtrl, databaseprovider, platform, navParams) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.databaseprovider = databaseprovider;
        this.platform = platform;
        this.navParams = navParams;
        this.developer = {};
        this.developers = [];
        this.databaseprovider.getDatabaseState().subscribe(function (rdy) {
            if (rdy) {
                _this.loadDeveloperData();
            }
        });
    }
    SqlPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad SqlPage');
    };
    SqlPage.prototype.loadDeveloperData = function () {
        var _this = this;
        this.databaseprovider.getAllDevelopers().then(function (data) {
            _this.developers = data;
        });
    };
    SqlPage.prototype.addDeveloper = function () {
        var _this = this;
        this.databaseprovider.addDeveloper(this.developer['name'], this.developer['skill'], parseInt(this.developer['yearsOfExperience']))
            .then(function (data) {
            _this.loadDeveloperData();
        });
        this.developer = {};
    };
    SqlPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'page-sql',template:/*ion-inline-start:"D:\Ionic Project\Project\src\pages\sql\sql.html"*/'<ion-header>\n  <ion-navbar>\n    <ion-title>\n      Developer Data\n    </ion-title>\n  </ion-navbar>\n</ion-header>\n \n<ion-content padding>\n  <ion-item>\n    <ion-label stacked>What\'s your name?</ion-label>\n    <ion-input [(ngModel)]="developer.name" placeholder="Developer Name"></ion-input>\n  </ion-item>\n  <ion-item>\n    <ion-label stacked>What\'s your special skill?</ion-label>\n    <ion-input [(ngModel)]="developer.skill" placeholder="Special Skill"></ion-input>\n  </ion-item>\n  <ion-item>\n    <ion-label stacked>How long have you been working?</ion-label>\n    <ion-input [(ngModel)]="developer.yearsOfExperience" placeholder="Years of experience"></ion-input>\n  </ion-item>\n  <button ion-button full (click)="addDeveloper()">Add Developer Info</button>\n \n  <ion-list>\n    <ion-item *ngFor="let dev of developers">\n      <h2>{{ dev.name }}</h2>\n      <p>{{ dev.yearsOfExperience }} years of {{ dev.skill }} Experience!</p>\n    </ion-item>\n  </ion-list>\n</ion-content>'/*ion-inline-end:"D:\Ionic Project\Project\src\pages\sql\sql.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["i" /* NavController */], __WEBPACK_IMPORTED_MODULE_2__providers_database_database__["a" /* DatabaseProvider */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["k" /* Platform */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* NavParams */]])
    ], SqlPage);
    return SqlPage;
}());

//# sourceMappingURL=sql.js.map

/***/ })

});
//# sourceMappingURL=1.js.map