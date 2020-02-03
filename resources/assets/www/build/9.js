webpackJsonp([9],{

/***/ 689:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "DetailsPageModule", function() { return DetailsPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__details__ = __webpack_require__(699);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var DetailsPageModule = /** @class */ (function () {
    function DetailsPageModule() {
    }
    DetailsPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["I" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__details__["a" /* DetailsPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["f" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__details__["a" /* DetailsPage */]),
            ],
        })
    ], DetailsPageModule);
    return DetailsPageModule;
}());

//# sourceMappingURL=details.module.js.map

/***/ }),

/***/ 699:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DetailsPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_forms__ = __webpack_require__(25);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__providers_formdet_formdet__ = __webpack_require__(351);
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
 * Generated class for the DetailsPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var DetailsPage = /** @class */ (function () {
    function DetailsPage(navCtrl, navParams, formbuilder, formdatProvider) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.formbuilder = formbuilder;
        this.formdatProvider = formdatProvider;
        this.accept = false;
        this.countries = ["Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Anguilla", "Antigua &amp; Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia &amp; Herzegovina", "Botswana", "Brazil", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Cape Verde", "Cayman Islands", "Chad", "Chile", "China", "Colombia", "Congo", "Cook Islands", "Costa Rica", "Cote D Ivoire", "Croatia", "Cruise Ship", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Estonia", "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Polynesia", "French West Indies", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guam", "Guatemala", "Guernsey", "Guinea", "Guinea Bissau", "Guyana", "Haiti", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kuwait", "Kyrgyz Republic", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Mauritania", "Mauritius", "Mexico", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Namibia", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Norway", "Oman", "Pakistan", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "Saint Pierre &amp; Miquelon", "Samoa", "San Marino", "Satellite", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "South Africa", "South Korea", "Spain", "Sri Lanka", "St Kitts &amp; Nevis", "St Lucia", "St Vincent", "St. Lucia", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor L'Este", "Togo", "Tonga", "Trinidad &amp; Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks &amp; Caicos", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "Uruguay", "Uzbekistan", "Venezuela", "Vietnam", "Virgin Islands (US)", "Yemen", "Zambia", "Zimbabwe"];
        this.mobnumPattern = "^((\\+91-?)|0)?[0-9]{10}$";
        this.firstnamePattern = "[a-zA-Z]{4,}$";
        this.lastnamePattern = "[a-zA-Z]{1,15}$";
        this.formgroup = formbuilder.group({
            firstname: ['', [__WEBPACK_IMPORTED_MODULE_2__angular_forms__["f" /* Validators */].required, __WEBPACK_IMPORTED_MODULE_2__angular_forms__["f" /* Validators */].pattern(this.firstnamePattern)]],
            lastname: ['', [__WEBPACK_IMPORTED_MODULE_2__angular_forms__["f" /* Validators */].required, __WEBPACK_IMPORTED_MODULE_2__angular_forms__["f" /* Validators */].pattern(this.lastnamePattern)]],
            mobile: ['', [__WEBPACK_IMPORTED_MODULE_2__angular_forms__["f" /* Validators */].required, __WEBPACK_IMPORTED_MODULE_2__angular_forms__["f" /* Validators */].pattern(this.mobnumPattern)]],
            email: ['', [__WEBPACK_IMPORTED_MODULE_2__angular_forms__["f" /* Validators */].required, __WEBPACK_IMPORTED_MODULE_2__angular_forms__["f" /* Validators */].email]],
            address: ['', __WEBPACK_IMPORTED_MODULE_2__angular_forms__["f" /* Validators */].required],
            country: ['', __WEBPACK_IMPORTED_MODULE_2__angular_forms__["f" /* Validators */].required],
        });
        this.firstname = this.formgroup.controls['firstname'];
        this.lastname = this.formgroup.controls['lastname'];
        this.mobile = this.formgroup.controls['mobile'];
        this.email = this.formgroup.controls['email'];
        this.address = this.formgroup.controls['address'];
        this.country = this.formgroup.controls['country'];
    }
    DetailsPage.prototype.formSubmit = function () {
        console.log(this.firstname.value, this.lastname.value, this.mobile.value, this.email.value, this.address.value, this.country.value);
        this.formdatProvider.formDetails(this.firstname, this.lastname, this.mobile, this.email, this.address, this.country);
        this.navCtrl.setRoot("MainPage");
    };
    DetailsPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad DetailsPage');
    };
    DetailsPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'page-details',template:/*ion-inline-start:"D:\Ionic Project\Project\src\pages\details\details.html"*/'<!--\n  Generated template for the DetailsPage page.\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n  Ionic pages and navigation.\n-->\n<ion-header>\n\n  <ion-navbar>\n    <ion-title>User Details</ion-title>\n  </ion-navbar>\n\n</ion-header>\n\n\n<ion-content padding>\n\n    <form [formGroup]="formgroup">\n      <ion-list>\n        <ion-item>\n          <ion-label stacked>\n            FirstName\n          </ion-label>\n          <ion-input type="text" formControlName="firstname"></ion-input>\n        </ion-item>\n        <ion-item *ngIf="firstname.hasError(\'required\') && firstname.touched">\n          <p> *FirstName is required</p>\n        </ion-item>\n        <ion-item *ngIf="firstname.hasError(\'pattern\') && firstname.touched">\n            <p>*FirstName not valid.(Minumum 4 characters)</p>\n            </ion-item>\n        <ion-item>\n            <ion-label stacked>\n              LastName\n            </ion-label>\n            <ion-input type="text" formControlName="lastname"></ion-input>\n        </ion-item>\n        <ion-item *ngIf="lastname.hasError(\'required\') && lastname.touched">\n            <p> *LastName is required</p>\n          </ion-item>\n          <ion-item *ngIf="lastname.hasError(\'pattern\') && lastname.touched">\n              <p>*LastName not valid.(Max 15 characters)</p>\n              </ion-item>\n          <ion-item>\n              <ion-label stacked>\n                Mobile Number\n              </ion-label>\n              <ion-input type="text" formControlName="mobile"></ion-input>\n            </ion-item>\n            <ion-item *ngIf="mobile.hasError(\'required\') && mobile.touched">\n              <p> *MobileNumber is required</p>\n            </ion-item>\n            <ion-item *ngIf="mobile.hasError(\'pattern\') && mobile.touched">\n                <p >*Mobile Number not valid.</p>\n                </ion-item>\n            <ion-item>\n                <ion-label stacked>\n                  Email ID\n                </ion-label>\n                <ion-input type="text" formControlName="email"></ion-input>\n              </ion-item>\n              <ion-item *ngIf="email.hasError(\'required\') && email.touched">\n                <p> *EMAIL ID is required</p>\n              </ion-item>\n              <ion-item *ngIf="email.hasError(\'email\') && email.touched">\n                  <p>*Email ID is not valid.</p>\n                  </ion-item>\n              <ion-item>\n                  <ion-label stacked>\n                    Address\n                  </ion-label>\n                  <ion-input type="text" formControlName="address"></ion-input>\n                </ion-item>\n                <ion-item *ngIf="address.hasError(\'required\') && address.touched">\n                  <p> *Address is required</p>\n                </ion-item>\n                <ion-item>\n                    <ion-label stacked>\n                      Country\n                    </ion-label>\n                    <ion-select formControlName="country">\n                    <ion-option value="">Country</ion-option>\n                    <ion-option *ngFor="let ctry of countries" [value]="[ctry]"> {{ctry}}\n                    </ion-option>\n                   </ion-select>\n                  </ion-item>\n                  <ion-item *ngIf="country.hasError(\'required\') && country.touched">\n                    <p> *Country is required</p>\n                  </ion-item>\n      </ion-list>\n    </form>\n    <ion-list>\n    <ion-item>\n        <ion-label>Accept the Terms and Condtions </ion-label>\n        <ion-toggle [(ngModel)]="accept"></ion-toggle>\n      </ion-item>\n      <ion-item *ngIf="!accept">\n          <p> *You must accept the Terms and Conditions!</p>\n        </ion-item>\n        <br/>\n        <div text-center>\n        <button ion-button (click)="formSubmit()">Submit</button>\n        </div>\n      </ion-list>\n</ion-content>\n'/*ion-inline-end:"D:\Ionic Project\Project\src\pages\details\details.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["i" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* NavParams */], __WEBPACK_IMPORTED_MODULE_2__angular_forms__["a" /* FormBuilder */], __WEBPACK_IMPORTED_MODULE_3__providers_formdet_formdet__["a" /* FormdetProvider */]])
    ], DetailsPage);
    return DetailsPage;
}());

//# sourceMappingURL=details.js.map

/***/ })

});
//# sourceMappingURL=9.js.map