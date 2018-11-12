package com.codingpixel.healingbudz.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jawadali on 11/15/17.
 */

public class StrainOverViewDataModel {
    String Strain_short_discription;
    int survey_budz_count;
    int get_user_review_count;
    //    int survey_budz_count;
    int get_user_servy_cout;
    ArrayList<Reviews> reviews;
    ArrayList<SurvayDataModal> madicalConditions;
    ArrayList<SurvayDataModal> Diseace_Prevention;
    ArrayList<SurvayDataModal> Modes_Sensation;
    ArrayList<SurvayDataModal> Negative_Effect;
    ArrayList<SurvayDataModal> Flavour_Profiles;

    public int getGet_user_servy_cout() {
        return get_user_servy_cout;
    }

    public void setGet_user_servy_cout(int get_user_servy_cout) {
        this.get_user_servy_cout = get_user_servy_cout;
    }

    public String getStrain_short_discription() {
        return Strain_short_discription;
    }

    public void setStrain_short_discription(String strain_short_discription) {
        Strain_short_discription = strain_short_discription;
    }

    public int getGet_user_review_count() {
        return get_user_review_count;
    }

    public void setGet_user_review_count(int get_user_review_count) {
        this.get_user_review_count = get_user_review_count;
    }

    public int getSurvey_budz_count() {
        return survey_budz_count;
    }

    public void setSurvey_budz_count(int survey_budz_count) {
        this.survey_budz_count = survey_budz_count;
    }

    public ArrayList<SurvayDataModal> getMadicalConditions() {
        return madicalConditions;
    }

    public void setMadicalConditions(ArrayList<SurvayDataModal> madicalConditions) {
        this.madicalConditions = madicalConditions;
    }

    public ArrayList<SurvayDataModal> getDiseace_Prevention() {
        return Diseace_Prevention;
    }

    public void setDiseace_Prevention(ArrayList<SurvayDataModal> diseace_Prevention) {
        Diseace_Prevention = diseace_Prevention;
    }

    public ArrayList<SurvayDataModal> getModes_Sensation() {
        return Modes_Sensation;
    }

    public void setModes_Sensation(ArrayList<SurvayDataModal> modes_Sensation) {
        Modes_Sensation = modes_Sensation;
    }

    public ArrayList<SurvayDataModal> getNegative_Effect() {
        return Negative_Effect;
    }

    public void setNegative_Effect(ArrayList<SurvayDataModal> negative_Effect) {
        Negative_Effect = negative_Effect;
    }

    public ArrayList<SurvayDataModal> getFlavour_Profiles() {
        return Flavour_Profiles;
    }

    public void setFlavour_Profiles(ArrayList<SurvayDataModal> flavour_Profiles) {
        Flavour_Profiles = flavour_Profiles;
    }

    //Survay answers
    ArrayList<MedicalConditionAnswers> medicalConditionAnswers;
    ArrayList<ModesAndSensationsAnswers> modesAndSensationsAnswers;
    ArrayList<NegativeEffectAnswers> negativeEffectAnswers;
    ArrayList<DiseasePreventionAnswers> diseasePreventionAnswers;
    ArrayList<SurvayFlavourAnswers> survayFlavourAnswers;


    public ArrayList<MedicalConditionAnswers> getMedicalConditionAnswers() {
        return medicalConditionAnswers;
    }

    public void setMedicalConditionAnswers(ArrayList<MedicalConditionAnswers> medicalConditionAnswers) {
        this.medicalConditionAnswers = medicalConditionAnswers;
    }

    public ArrayList<ModesAndSensationsAnswers> getModesAndSensationsAnswers() {
        return modesAndSensationsAnswers;
    }

    public void setModesAndSensationsAnswers(ArrayList<ModesAndSensationsAnswers> modesAndSensationsAnswers) {
        this.modesAndSensationsAnswers = modesAndSensationsAnswers;
    }

    public ArrayList<NegativeEffectAnswers> getNegativeEffectAnswers() {
        return negativeEffectAnswers;
    }

    public void setNegativeEffectAnswers(ArrayList<NegativeEffectAnswers> negativeEffectAnswers) {
        this.negativeEffectAnswers = negativeEffectAnswers;
    }

    public ArrayList<DiseasePreventionAnswers> getDiseasePreventionAnswers() {
        return diseasePreventionAnswers;
    }

    public void setDiseasePreventionAnswers(ArrayList<DiseasePreventionAnswers> diseasePreventionAnswers) {
        this.diseasePreventionAnswers = diseasePreventionAnswers;
    }

    public ArrayList<SurvayFlavourAnswers> getSurvayFlavourAnswers() {
        return survayFlavourAnswers;
    }

    public void setSurvayFlavourAnswers(ArrayList<SurvayFlavourAnswers> survayFlavourAnswers) {
        this.survayFlavourAnswers = survayFlavourAnswers;
    }

    public ArrayList<Reviews> getReviews() {
        return reviews;
    }

    public String getStrain_short_Discription() {
        return Strain_short_discription;
    }

    public void setStrain_short_Discription(String strain_short_Review) {
        Strain_short_discription = strain_short_Review;
    }

    public void setReviews(ArrayList<Reviews> reviews) {
        this.reviews = reviews;
    }

    public static class Reviews implements Parcelable {
        int id;
        int strain_id;
        int reviewed_by;
        String review;
        String created_at;
        String updated_at;
        int user_id;
        String user_first_name;
        String user_image_path;
        String user_avatar;
        String special_icon;
        double rating;
        int user_point;
        boolean isLiked;
        int total_likes;

        public int getTotal_likes() {
            return total_likes;
        }

        public void setTotal_likes(int total_likes) {
            this.total_likes = total_likes;
        }

        public boolean isLiked() {
            return isLiked;
        }

        public void setLiked(boolean liked) {
            isLiked = liked;
        }

        public int getUser_point() {
            return user_point;
        }

        public void setUser_point(int user_point) {
            this.user_point = user_point;
        }

        public String getSpecial_icon() {
            return special_icon;
        }

        public void setSpecial_icon(String special_icon) {
            this.special_icon = special_icon;
        }

        public int getIs_user_flaged_count() {
            return is_user_flaged_count;
        }

        public void setIs_user_flaged_count(int is_user_flaged_count) {
            this.is_user_flaged_count = is_user_flaged_count;
        }

        int is_user_flaged_count;
        String attatchment_type;
        String attatchment_poster;
        String attatchment_path;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStrain_id() {
            return strain_id;
        }

        public void setStrain_id(int strain_id) {
            this.strain_id = strain_id;
        }

        public int getReviewed_by() {
            return reviewed_by;
        }

        public void setReviewed_by(int reviewed_by) {
            this.reviewed_by = reviewed_by;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_first_name() {
            return user_first_name;
        }

        public void setUser_first_name(String user_first_name) {
            this.user_first_name = user_first_name;
        }

        public String getUser_image_path() {
            return user_image_path;
        }

        public void setUser_image_path(String user_image_path) {
            this.user_image_path = user_image_path;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public int getRating() {
            return (int) rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getAttatchment_type() {
            return attatchment_type;
        }

        public void setAttatchment_type(String attatchment_type) {
            this.attatchment_type = attatchment_type;
        }

        public String getAttatchment_poster() {
            return attatchment_poster;
        }

        public void setAttatchment_poster(String attatchment_poster) {
            this.attatchment_poster = attatchment_poster;
        }

        public String getAttatchment_path() {
            return attatchment_path;
        }

        public void setAttatchment_path(String attatchment_path) {
            this.attatchment_path = attatchment_path;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.strain_id);
            dest.writeInt(this.reviewed_by);
            dest.writeString(this.review);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeInt(this.user_id);
            dest.writeString(this.user_first_name);
            dest.writeString(this.user_image_path);
            dest.writeString(this.user_avatar);
            dest.writeString(this.special_icon);
            dest.writeDouble(this.rating);
            dest.writeInt(this.is_user_flaged_count);
            dest.writeString(this.attatchment_type);
            dest.writeString(this.attatchment_poster);
            dest.writeString(this.attatchment_path);
            dest.writeInt(this.user_point);
            dest.writeInt(this.total_likes);
        }

        public Reviews() {
        }

        protected Reviews(Parcel in) {
            this.id = in.readInt();
            this.strain_id = in.readInt();
            this.reviewed_by = in.readInt();
            this.review = in.readString();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.user_id = in.readInt();
            this.user_first_name = in.readString();
            this.user_image_path = in.readString();
            this.user_avatar = in.readString();
            this.special_icon = in.readString();
            this.rating = in.readDouble();
            this.is_user_flaged_count = in.readInt();
            this.attatchment_type = in.readString();
            this.attatchment_poster = in.readString();
            this.attatchment_path = in.readString();
            this.user_point = in.readInt();
            this.total_likes = in.readInt();
        }

        public static final Parcelable.Creator<Reviews> CREATOR = new Parcelable.Creator<Reviews>() {
            @Override
            public Reviews createFromParcel(Parcel source) {
                return new Reviews(source);
            }

            @Override
            public Reviews[] newArray(int size) {
                return new Reviews[size];
            }
        };
    }

    public static class SurvayDataModal {
        int result;
        String name;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    //@Survay data modal
    public static class MedicalConditionAnswers {
        int id;
        String m_condition;
        int is_approved;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getM_condition() {
            return m_condition;
        }

        public void setM_condition(String m_condition) {
            this.m_condition = m_condition;
        }

        public int getIs_approved() {
            return is_approved;
        }

        public void setIs_approved(int is_approved) {
            this.is_approved = is_approved;
        }
    }

    public static class ModesAndSensationsAnswers {
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSensation() {
            return sensation;
        }

        public void setSensation(String sensation) {
            this.sensation = sensation;
        }

        public int getIs_approved() {
            return is_approved;
        }

        public void setIs_approved(int is_approved) {
            this.is_approved = is_approved;
        }

        String sensation;
        int is_approved;

    }

    public static class NegativeEffectAnswers {
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEffect() {
            return effect;
        }

        public void setEffect(String effect) {
            this.effect = effect;
        }

        public int getIs_approved() {
            return is_approved;
        }

        public void setIs_approved(int is_approved) {
            this.is_approved = is_approved;
        }

        String effect;
        int is_approved;

    }

    public static class DiseasePreventionAnswers {
        int id;
        String prevention;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPrevention() {
            return prevention;
        }

        public void setPrevention(String prevention) {
            this.prevention = prevention;
        }

        public int getIs_approved() {
            return is_approved;
        }

        public void setIs_approved(int is_approved) {
            this.is_approved = is_approved;
        }

        int is_approved;

    }

    public static class SurvayFlavourAnswers {
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFlavor() {
            return flavor;
        }

        public void setFlavor(String flavor) {
            this.flavor = flavor;
        }

        public int getIs_approved() {
            return is_approved;
        }

        public void setIs_approved(int is_approved) {
            this.is_approved = is_approved;
        }

        String flavor;
        int is_approved;

    }


}

