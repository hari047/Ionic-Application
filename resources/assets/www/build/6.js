webpackJsonp([6],{

/***/ 692:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "IndexPageModule", function() { return IndexPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__index__ = __webpack_require__(702);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var IndexPageModule = /** @class */ (function () {
    function IndexPageModule() {
    }
    IndexPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["I" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__index__["a" /* IndexPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__index__["a" /* IndexPage */]),
            ],
        })
    ], IndexPageModule);
    return IndexPageModule;
}());

//# sourceMappingURL=index.module.js.map

/***/ }),

/***/ 702:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return IndexPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__providers_auth_auth__ = __webpack_require__(353);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


// import { HttpClient } from '@angular/common/http'; private http:HttpClient,


/**
 * Generated class for the IndexPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var IndexPage = /** @class */ (function () {
    function IndexPage(navCtrl, navParams, events, authProvider) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.events = events;
        this.authProvider = authProvider;
        this.fity = [];
        this.cities = [{
                "country": "India",
                "capital": "New Delhi",
                "img": "https://c1.hiqcdn.com/customcdn/1024x768/uploadimages/travel/New_Delhi_irctc_cover.jpg"
            },
            {
                "country": "United Kingdom",
                "capital": "London",
                "img": "https://travel.home.sndimg.com/content/dam/images/travel/fullset/2015/05/28/big-ben-london-england.jpg.rend.hgtvcom.1280.960.suffix/1491582155388.jpeg"
            },
            {
                "country": "United States",
                "capital": "Washington D.C.",
                "img": "https://cdn.history.com/sites/2/2013/11/Washington-DC-hero-H.jpeg"
            },
            {
                "country": "Russia",
                "capital": "Moscow",
                "img": "https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/Moscow_July_2011-49.jpg/860px-Moscow_July_2011-49.jpg"
            },
            {
                "country": "Saudi Arabia",
                "capital": "Riyadh",
                "img": "http://www.thehindu.com/news/international/article21937415.ece/alternates/FREE_660/RIYADHPALACE"
            },
            {
                "country": "Spain",
                "capital": "Madrid",
                "img": "https://d1bvpoagx8hqbg.cloudfront.net/originals/martas-experience-in-madrid-spain-67b359852696d54b2b3dbe5f28622288.jpg"
            },
            {
                "country": "France",
                "capital": "Paris",
                "img": "https://lonelyplanetimages.imgix.net/mastheads/GettyImages-500759045_super.jpg?sharp=10&vib=20&w=1200"
            },
            {
                "country": "Brazil",
                "capital": "Brasilia",
                "img": "http://mekkalabs.com.br/theguide/wp-content/uploads/2018/02/brasilia-dois.jpg"
            },
            {
                "country": "Canada",
                "capital": "Ottawa",
                "img": "https://assets.vogue.com/photos/59a43937cf3bf91c16da5b5e/16:9/pass/00-social-tout-ottawa-canada-healthy-travel-guide.jpg"
            },
            {
                "country": "Germany",
                "capital": "Berlin",
                "img": "http://designedtours.com/wp-content/uploads/2018/04/alemania_berlin_shutterstockrf_566050516_patino_shutterstock.jpg"
            },
        ];
    }
    IndexPage.prototype.createUser = function (user) {
        console.log('User created!');
        this.events.publish('user:created', user, Date.now());
        this.authProvider.getSampleData();
        var dat = JSON.stringify(this.authProvider.sendSampleData());
        console.log(dat);
        var dat2 = JSON.parse(dat);
        console.log(dat2);
        this.fity = this.authProvider.returnJson();
    };
    IndexPage.prototype.profileDirect = function () {
        this.navCtrl.setRoot("ProfilePage");
    };
    IndexPage.prototype.setCities = function () {
        this.cities = [{
                "country": "India",
                "capital": "New Delhi",
                "img": "https://c1.hiqcdn.com/customcdn/1024x768/uploadimages/travel/New_Delhi_irctc_cover.jpg"
            },
            {
                "country": "United Kingdom",
                "capital": "London",
                "img": "https://travel.home.sndimg.com/content/dam/images/travel/fullset/2015/05/28/big-ben-london-england.jpg.rend.hgtvcom.1280.960.suffix/1491582155388.jpeg"
            },
            {
                "country": "United States",
                "capital": "Washington D.C.",
                "img": "https://cdn.history.com/sites/2/2013/11/Washington-DC-hero-H.jpeg"
            },
            {
                "country": "Russia",
                "capital": "Moscow",
                "img": "https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/Moscow_July_2011-49.jpg/860px-Moscow_July_2011-49.jpg"
            },
            {
                "country": "Saudi Arabia",
                "capital": "Riyadh",
                "img": "http://www.thehindu.com/news/international/article21937415.ece/alternates/FREE_660/RIYADHPALACE"
            },
            {
                "country": "Spain",
                "capital": "Madrid",
                "img": "https://d1bvpoagx8hqbg.cloudfront.net/originals/martas-experience-in-madrid-spain-67b359852696d54b2b3dbe5f28622288.jpg"
            },
            {
                "country": "France",
                "capital": "Paris",
                "img": "https://lonelyplanetimages.imgix.net/mastheads/GettyImages-500759045_super.jpg?sharp=10&vib=20&w=1200"
            },
            {
                "country": "Brazil",
                "capital": "Brasilia",
                "img": "http://mekkalabs.com.br/theguide/wp-content/uploads/2018/02/brasilia-dois.jpg"
            },
            {
                "country": "Canada",
                "capital": "Ottawa",
                "img": "https://assets.vogue.com/photos/59a43937cf3bf91c16da5b5e/16:9/pass/00-social-tout-ottawa-canada-healthy-travel-guide.jpg"
            },
            {
                "country": "Germany",
                "capital": "Berlin",
                "img": "http://designedtours.com/wp-content/uploads/2018/04/alemania_berlin_shutterstockrf_566050516_patino_shutterstock.jpg"
            },];
    };
    IndexPage.prototype.filterItems = function (ev) {
        this.setCities();
        var val = ev.target.value;
        if (val && val.trim() !== '') {
            this.cities = this.cities.filter(function (city) {
                return city.country.toLowerCase().includes(val.toLowerCase());
            });
        }
    };
    IndexPage.prototype.filterItemsC = function (ev) {
        this.setCities();
        var val = ev.target.value;
        if (val && val.trim() !== '') {
            this.cities = this.cities.filter(function (city) {
                return city.capital.toLowerCase().includes(val.toLowerCase());
            });
        }
    };
    IndexPage.prototype.ngOnInit = function () {
        this.setCities();
    };
    IndexPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad ionViewDidLoad');
        this.createUser("hari");
    };
    IndexPage.prototype.ionViewWillLoad = function () {
        console.log('ionViewDidLoad ionViewWillLoad');
    };
    IndexPage.prototype.ionViewWillEnter = function () {
        console.log('ionViewDidLoad ionViewWillEnter');
    };
    IndexPage.prototype.ionViewDidEnter = function () {
        console.log('ionViewDidLoad ionViewDidEnter');
    };
    IndexPage.prototype.ionViewWillLeave = function () {
        console.log('ionViewDidLoad ionViewWillLeave');
    };
    IndexPage.prototype.ionViewDidLeave = function () {
        console.log('ionViewDidLoad ionViewDidLeave');
    };
    IndexPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'page-index',template:/*ion-inline-start:"D:\Ionic Project\Project\src\pages\index\index.html"*/'<!--\n  Generated template for the IndexPage page.\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n  Ionic pages and navigation.\n-->\n<ion-header>\n\n  <ion-navbar>\n    <ion-title>Index</ion-title>\n  </ion-navbar>\n\n</ion-header>\n\n\n<ion-content padding>\n  <ion-searchbar placeholder="Filter Countries" showCancelButton color="danger" (ionInput)="filterItems($event)"></ion-searchbar>\n  <ion-item *ngFor="let city of cities">\n      <ion-card>\n      <p>Country</p>\n      <h6>{{ city?.country}}</h6>\n      <p>Capital</p>\n      <h6>{{ city?.capital}}</h6>\n      <p>Image</p>\n      <img src={{city?.img}} />\n      </ion-card> \n  </ion-item>\n  <ion-searchbar placeholder="Filter Cities(Capital)" (ionInput)="filterItemsC($event)"></ion-searchbar>\n      <!-- <ion-item *ngFor="let fit of fity">\n          <p>Country</p>\n          <h6>{{ fit?.country}}</h6>\n          <p>Capital</p>\n          <h6>{{ fit?.capital}}</h6>\n          <p>Image</p>\n          <img src={{fit?.img}} />\n        </ion-item>  -->\n  <button ion-button  color="blue" round (click)="profileDirect()">Setup Profile -></button>\n</ion-content>\n'/*ion-inline-end:"D:\Ionic Project\Project\src\pages\index\index.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["i" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* NavParams */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["b" /* Events */], __WEBPACK_IMPORTED_MODULE_2__providers_auth_auth__["a" /* AuthProvider */]])
    ], IndexPage);
    return IndexPage;
}());

//# sourceMappingURL=index.js.map

/***/ })

});
//# sourceMappingURL=6.js.map