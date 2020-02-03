package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public class ShareCameraEffectContent extends ShareContent<ShareCameraEffectContent, Builder> {
    public static final Creator<ShareCameraEffectContent> CREATOR = new Creator<ShareCameraEffectContent>() {
        public ShareCameraEffectContent createFromParcel(Parcel in) {
            return new ShareCameraEffectContent(in);
        }

        public ShareCameraEffectContent[] newArray(int size) {
            return new ShareCameraEffectContent[size];
        }
    };
    private CameraEffectArguments arguments;
    private String effectId;
    private CameraEffectTextures textures;

    public static final class Builder extends com.facebook.share.model.ShareContent.Builder<ShareCameraEffectContent, Builder> {
        /* access modifiers changed from: private */
        public CameraEffectArguments arguments;
        /* access modifiers changed from: private */
        public String effectId;
        /* access modifiers changed from: private */
        public CameraEffectTextures textures;

        public Builder setEffectId(String effectId2) {
            this.effectId = effectId2;
            return this;
        }

        public Builder setArguments(CameraEffectArguments arguments2) {
            this.arguments = arguments2;
            return this;
        }

        public Builder setTextures(CameraEffectTextures textures2) {
            this.textures = textures2;
            return this;
        }

        public ShareCameraEffectContent build() {
            return new ShareCameraEffectContent(this);
        }

        /* Debug info: failed to restart local var, previous not found, register: 2 */
        public Builder readFrom(ShareCameraEffectContent model) {
            if (model == null) {
                return this;
            }
            return ((Builder) super.readFrom(model)).setEffectId(this.effectId).setArguments(this.arguments);
        }
    }

    private ShareCameraEffectContent(Builder builder) {
        super((com.facebook.share.model.ShareContent.Builder) builder);
        this.effectId = builder.effectId;
        this.arguments = builder.arguments;
        this.textures = builder.textures;
    }

    ShareCameraEffectContent(Parcel in) {
        super(in);
        this.effectId = in.readString();
        this.arguments = new com.facebook.share.model.CameraEffectArguments.Builder().readFrom(in).build();
        this.textures = new com.facebook.share.model.CameraEffectTextures.Builder().readFrom(in).build();
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(this.effectId);
        out.writeParcelable(this.arguments, 0);
        out.writeParcelable(this.textures, 0);
    }

    public String getEffectId() {
        return this.effectId;
    }

    public CameraEffectArguments getArguments() {
        return this.arguments;
    }

    public CameraEffectTextures getTextures() {
        return this.textures;
    }
}
