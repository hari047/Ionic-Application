package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;

public final class ShareOpenGraphContent extends ShareContent<ShareOpenGraphContent, Builder> {
    public static final Creator<ShareOpenGraphContent> CREATOR = new Creator<ShareOpenGraphContent>() {
        public ShareOpenGraphContent createFromParcel(Parcel in) {
            return new ShareOpenGraphContent(in);
        }

        public ShareOpenGraphContent[] newArray(int size) {
            return new ShareOpenGraphContent[size];
        }
    };
    private final ShareOpenGraphAction action;
    private final String previewPropertyName;

    public static final class Builder extends com.facebook.share.model.ShareContent.Builder<ShareOpenGraphContent, Builder> {
        /* access modifiers changed from: private */
        public ShareOpenGraphAction action;
        /* access modifiers changed from: private */
        public String previewPropertyName;

        public Builder setAction(@Nullable ShareOpenGraphAction action2) {
            ShareOpenGraphAction build;
            if (action2 == null) {
                build = null;
            } else {
                build = new com.facebook.share.model.ShareOpenGraphAction.Builder().readFrom(action2).build();
            }
            this.action = build;
            return this;
        }

        public Builder setPreviewPropertyName(@Nullable String previewPropertyName2) {
            this.previewPropertyName = previewPropertyName2;
            return this;
        }

        public ShareOpenGraphContent build() {
            return new ShareOpenGraphContent(this);
        }

        /* Debug info: failed to restart local var, previous not found, register: 2 */
        public Builder readFrom(ShareOpenGraphContent model) {
            if (model == null) {
                return this;
            }
            return ((Builder) super.readFrom(model)).setAction(model.getAction()).setPreviewPropertyName(model.getPreviewPropertyName());
        }
    }

    private ShareOpenGraphContent(Builder builder) {
        super((com.facebook.share.model.ShareContent.Builder) builder);
        this.action = builder.action;
        this.previewPropertyName = builder.previewPropertyName;
    }

    ShareOpenGraphContent(Parcel in) {
        super(in);
        this.action = new com.facebook.share.model.ShareOpenGraphAction.Builder().readFrom(in).build();
        this.previewPropertyName = in.readString();
    }

    @Nullable
    public ShareOpenGraphAction getAction() {
        return this.action;
    }

    @Nullable
    public String getPreviewPropertyName() {
        return this.previewPropertyName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeParcelable(this.action, 0);
        out.writeString(this.previewPropertyName);
    }
}
