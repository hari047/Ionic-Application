webpackJsonp([4],{

/***/ 694:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LocationPageModule", function() { return LocationPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__location__ = __webpack_require__(704);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var LocationPageModule = /** @class */ (function () {
    function LocationPageModule() {
    }
    LocationPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["I" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__location__["a" /* LocationPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__location__["a" /* LocationPage */]),
            ],
        })
    ], LocationPageModule);
    return LocationPageModule;
}());

//# sourceMappingURL=location.module.js.map

/***/ }),

/***/ 704:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LocationPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__providers_pict_pict__ = __webpack_require__(350);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__ionic_native_geolocation__ = __webpack_require__(354);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__ionic_native_network__ = __webpack_require__(355);
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
 * Generated class for the LocationPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var LocationPage = /** @class */ (function () {
    function LocationPage(navCtrl, navParams, pictProvider, geolocation, network) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.pictProvider = pictProvider;
        this.geolocation = geolocation;
        this.network = network;
        this.dateArr = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
        this.safari = 'Network';
        this.items = {
            'Network': [
                {
                    name: 'Status',
                },
                {
                    name: 'Type',
                }
            ],
            'Location': [
                {
                    name: 'Current Location'
                },
                {
                    name: 'Lattitude'
                },
                {
                    name: 'Longitude'
                }
            ],
        };
        this.currentdate = new Date();
        this.getFormattedDate();
    }
    LocationPage.prototype.ionViewDidLoad = function () {
        var _this = this;
        console.log('ionViewDidLoad LocationPage');
        this.network.onDisconnect().subscribe(function (data) {
            console.log(data, "network was disconnected :-(");
            _this.status = 'Disconnected';
            _this.ntype = _this.network.type;
        });
        // this.network.onchange().subscribe((data) => {
        //   console.log(data,"ddddddddd");
        //   console.log('network connected!');
        //   // this.status = data.type=="online"?"connected":"disconnected";
        //     this.ntype = this.network.type;
        //     console.log(this.network.type,"ntype");
        //     if (this.network.type === 'wifi') {
        //       console.log('we got a wifi connection, woohoo!');
        //     }
        // });
        this.network.onConnect().subscribe(function (data) {
            _this.status = 'Connected';
            _this.ntype = _this.network.type;
            console.log(data, "ddddddddd");
            console.log('network connecteddd!');
        });
        this.pic = this.pictProvider.myPhoto;
        this.geolocation.getCurrentPosition().then(function (pos) {
            _this.lat = pos.coords.latitude;
            _this.lon = pos.coords.longitude;
        }).catch(function (err) { return console.log(err); });
    };
    LocationPage.prototype.getFormattedDate = function () {
        var dateObj = new Date();
        var year = dateObj.getFullYear().toString();
        var month = dateObj.getMonth().toString();
        var date = dateObj.getDate().toString();
        this.formattedDate = date + ' ' + this.dateArr[month] + ' ' + year;
    };
    LocationPage.prototype.detailsDirect = function () {
        this.navCtrl.setRoot("DetailsPage");
    };
    LocationPage.prototype.getSafariItems = function (type) {
        return this.items[type];
    };
    LocationPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'page-location',template:/*ion-inline-start:"D:\Ionic Project\Project\src\pages\location\location.html"*/'<!--\n  Generated template for the LocationPage page.\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n  Ionic pages and navigation.\n-->\n<ion-header>\n\n  <ion-navbar>\n    <ion-title>Profile</ion-title>\n  </ion-navbar>\n\n</ion-header>\n\n<ion-content class="cards-bg social-cards">\n\n<ion-card>\n\n    <ion-label padding>\n        {{formattedDate}}\n      </ion-label>\n    <ion-item>\n      <ion-avatar item-start>\n          <img src="{{ pic }}"/>\n      </ion-avatar>\n      <h2>Admin</h2>\n      <p>April 14, 1997</p>\n    </ion-item>\n  \n    <img src="{{ pic }}"/>\n  \n    <ion-card-content>\n      <p>This is your Profile Card.</p>\n    </ion-card-content>\n  \n    <ion-row>\n      <ion-col>\n        <button ion-button color="primary" clear small icon-start>\n          <ion-icon name=\'thumbs-up\'></ion-icon>\n          47 Likes\n        </button>\n      </ion-col>\n      <ion-col>\n        <button ion-button color="primary" clear small icon-start>\n          <ion-icon name=\'text\'></ion-icon>\n          100 Comments\n        </button>\n      </ion-col>\n      <ion-col align-self-center text-center>\n        <ion-note>\n          15h ago\n        </ion-note>\n      </ion-col>\n    </ion-row>\n  \n  </ion-card>\n\n  <ion-card>\n      <ion-item>\n          <ion-avatar item-start>\n              <img src="{{ pic }}"/>\n          </ion-avatar>\n          <h2>Admin</h2>\n          <h3> Current Location </h3>\n    <p> Lattitude : {{ lat }}</p>\n    <p> Longitude : {{ lon }}</p>\n  </ion-item>\n  </ion-card>\n\n  <ion-card>\n      <ion-item>\n          <ion-avatar item-start>\n              <img src="{{ pic }}"/>\n          </ion-avatar>\n          <h2>Admin</h2>\n          <h3> Current Network Status </h3>\n    <p> Connection Status : {{ status }}</p>\n    <p> Connection Type : {{ ntype }}</p>\n  </ion-item>\n  </ion-card>\n  \n  \n  <br/>\n  <div text-center>\n  <button ion-button  color="blue" round (click)="detailsDirect()">Enter Details</button>\n  </div>\n\n<!-- <ion-card>\n    <ion-card-header>\n      {{ safari }}\n    </ion-card-header>\n    <ion-card-content>\n      <ion-segment [(ngModel)]="safari" color="dark">\n        <ion-segment-button value="Location">\n          <ion-icon name="compass"></ion-icon>\n        </ion-segment-button>\n        <ion-segment-button value="Network">\n          <ion-icon name="wifi"></ion-icon>\n        </ion-segment-button>\n      </ion-segment>\n      <ion-list style="margin: 0" inset>\n        <button ion-item *ngFor="let sItem of getSafariItems(safari)">\n          {{ sItem.name }}\n        </button>\n      </ion-list>\n    </ion-card-content>\n  </ion-card> -->\n\n</ion-content>'/*ion-inline-end:"D:\Ionic Project\Project\src\pages\location\location.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["i" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* NavParams */], __WEBPACK_IMPORTED_MODULE_2__providers_pict_pict__["a" /* PictProvider */], __WEBPACK_IMPORTED_MODULE_3__ionic_native_geolocation__["a" /* Geolocation */], __WEBPACK_IMPORTED_MODULE_4__ionic_native_network__["a" /* Network */]])
    ], LocationPage);
    return LocationPage;
}());

//# sourceMappingURL=location.js.map

/***/ })

});
//# sourceMappingURL=4.js.map