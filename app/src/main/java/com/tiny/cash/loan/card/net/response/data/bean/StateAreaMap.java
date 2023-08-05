package com.tiny.cash.loan.card.net.response.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StateAreaMap implements Serializable {

    /**
     * dictMap : {"area":{"130":[{"key":"130001","val":"Afijio"},{"key":"130002","val":"Akinyele"},{"key":"130003","val":"Atiba"},{"key":"130004","val":"Atisbo"},{"key":"130005","val":"Egbeda"},{"key":"130006","val":"Ibadan North"},{"key":"130007","val":"Ibadan North-East"},{"key":"130008","val":"Ibadan North-West"},{"key":"130009","val":"Ibadan South-East"},{"key":"130010","val":"Ibadan South-West"},{"key":"130011","val":"Ibarapa Central"},{"key":"130012","val":"Ibarapa East"},{"key":"130013","val":"Ibarapa North"},{"key":"130014","val":"Ido"},{"key":"130015","val":"Irepo"},{"key":"130016","val":"Iseyin"},{"key":"130017","val":"Itesiwaju"},{"key":"130018","val":"Iwajowa"},{"key":"130019","val":"Kajola"},{"key":"130020","val":"Lagelu"},{"key":"130021","val":"Ogbomosho North"},{"key":"130022","val":"Ogbomosho South"},{"key":"130023","val":"Ogo Oluwa"},{"key":"130024","val":"Olorunsogo"},{"key":"130025","val":"Oluyole"},{"key":"130026","val":"Ona Ara"},{"key":"130027","val":"Orelope"},{"key":"130028","val":"Ori Ire"},{"key":"130029","val":"Oyo East"},{"key":"130030","val":"Oyo West"},{"key":"130031","val":"Saki East"},{"key":"130032","val":"Saki West"},{"key":"130033","val":"Surulere"}],"131":[{"key":"131001","val":"Barkin Ladi"},{"key":"131002","val":"Bassa"},{"key":"131003","val":"Bokkos"},{"key":"131004","val":"Jos East"},{"key":"131005","val":"Jos North"},{"key":"131006","val":"Jos South"},{"key":"131007","val":"Kanam"},{"key":"131008","val":"Kanke"},{"key":"131009","val":"Langtang North"},{"key":"131010","val":"Langtang South"},{"key":"131011","val":"Mangu"},{"key":"131012","val":"Mikang"},{"key":"131013","val":"Pankshin"},{"key":"131014","val":"Qua'an Pan"},{"key":"131015","val":"Riyom"},{"key":"131016","val":"Shendam"},{"key":"131017","val":"Wase"}],"110":[{"key":"110001","val":"Abakaliki"},{"key":"110002","val":"Afikpo North"},{"key":"110003","val":"Afikpo South (Edda)"},{"key":"110004","val":"Ebonyi"},{"key":"110005","val":"Ezza North"},{"key":"110006","val":"Ezza South"},{"key":"110007","val":"Ikwo"},{"key":"110008","val":"Ishielu"},{"key":"110009","val":"Ivo"},{"key":"110010","val":"Izzi"},{"key":"110011","val":"Ohaozara"},{"key":"110012","val":"Ohaukwu"},{"key":"110013","val":"Onicha"}],"132":[{"key":"132001","val":"Abua/Odual"},{"key":"132002","val":"Ahoada East"},{"key":"132003","val":"Ahoada West"},{"key":"132004","val":"Akuku-Toru"},{"key":"132005","val":"Andoni"},{"key":"132006","val":"Asari-Toru"},{"key":"132007","val":"Bonny"},{"key":"132008","val":"Degema"},{"key":"132009","val":"Eleme"},{"key":"132010","val":"Emuoha"},{"key":"132011","val":"Etche"},{"key":"132012","val":"Gokana"},{"key":"132013","val":"Ikwerre"},{"key":"132014","val":"Khana"},{"key":"132015","val":"Obio/Akpor"},{"key":"132016","val":"Obio/Akpor"},{"key":"132017","val":"Ogba/Egbema/Ndoni"},{"key":"132018","val":"Ogu/Bolo"},{"key":"132019","val":"Okrika"},{"key":"132020","val":"Omuma"},{"key":"132021","val":"Opobo/Nkoro"},{"key":"132022","val":"Oyigbo"},{"key":"132023","val":"Port Harcourt"},{"key":"132024","val":"Tai"}],"111":[{"key":"111001","val":"Akoko-Edo"},{"key":"111002","val":"Egor"},{"key":"111003","val":"Esan Central"},{"key":"111004","val":"Esan North-East"},{"key":"111005","val":"Esan South-East"},{"key":"111006","val":"Esan West"},{"key":"111007","val":"Etsako Central"},{"key":"111008","val":"Etsako East"},{"key":"111009","val":"Etsako West"},{"key":"111010","val":"Igueben"},{"key":"111011","val":"Ikpoba Okha"},{"key":"111012","val":"Oredo"},{"key":"111013","val":"Orhionmwon"},{"key":"111014","val":"Ovia North-East"},{"key":"111015","val":"Ovia South-West"},{"key":"111016","val":"Owan East"},{"key":"111017","val":"Owan West"},{"key":"111018","val":"Uhunmwonde"}],"133":[{"key":"133001","val":"Binji"},{"key":"133002","val":"Bodinga"},{"key":"133003","val":"Dange Shuni"},{"key":"133004","val":"Gada"},{"key":"133005","val":"Goronyo"},{"key":"133006","val":"Gudu"},{"key":"133007","val":"Gwadabawa"},{"key":"133008","val":"Illela"},{"key":"133009","val":"Isa"},{"key":"133010","val":"Kebbe"},{"key":"133011","val":"Kware"},{"key":"133012","val":"Rabah"},{"key":"133013","val":"Sabon Birni"},{"key":"133014","val":"Shagari"},{"key":"133015","val":"Silame"},{"key":"133016","val":"Sokoto North"},{"key":"133017","val":"Sokoto South"},{"key":"133018","val":"Tambuwal"},{"key":"133019","val":"Tangaza"},{"key":"133020","val":"Tureta"},{"key":"133021","val":"Wamako"},{"key":"133022","val":"Wurno"},{"key":"133023","val":"Yabo"}],"112":[{"key":"112001","val":"Ado Ekiti"},{"key":"112002","val":"Efon"},{"key":"112003","val":"Ekiti East"},{"key":"112004","val":"Ekiti South-West"},{"key":"112005","val":"Ekiti West"},{"key":"112006","val":"Emure"},{"key":"112007","val":"Gbonyin"},{"key":"112008","val":"Ido Osi"},{"key":"112009","val":"Ijero"},{"key":"112010","val":"Ikere"},{"key":"112011","val":"Ikole"},{"key":"112012","val":"Ilejemeje"},{"key":"112013","val":"Irepodun/Ifelodun"},{"key":"112014","val":"Ise/Orun"},{"key":"112015","val":"Moba"},{"key":"112016","val":"Oye"}],"134":[{"key":"134001","val":"Ardo Kola"},{"key":"134002","val":"Bali"},{"key":"134003","val":"Donga"},{"key":"134004","val":"Gashaka"},{"key":"134005","val":"Gassol"},{"key":"134006","val":"Ibi"},{"key":"134007","val":"Jalingo"},{"key":"134008","val":"Karim Lamido"},{"key":"134009","val":"Kumi"},{"key":"134010","val":"Lau"},{"key":"134011","val":"Sardauna"},{"key":"134012","val":"Takum"},{"key":"134013","val":"Ussa"},{"key":"134014","val":"Wukari"},{"key":"134015","val":"Yorro"},{"key":"134016","val":"Zing"}],"113":[{"key":"113001","val":"Aninri"},{"key":"113002","val":"Awgu"},{"key":"113003","val":"Enugu East"},{"key":"113004","val":"Enugu North"},{"key":"113005","val":"Enugu South"},{"key":"113006","val":"Ezeagu"},{"key":"113007","val":"Igbo Etiti"},{"key":"113008","val":"Igbo Eze North"},{"key":"113009","val":"Igbo Eze South"},{"key":"113010","val":"Isi Uzo"},{"key":"113011","val":"Nkanu East"},{"key":"113012","val":"Nkanu West"},{"key":"113013","val":"Nsukka"},{"key":"113014","val":"Oji River"},{"key":"113015","val":"Udenu"},{"key":"113016","val":"Udi"},{"key":"113017","val":"Uzo-Uwani"}],"135":[{"key":"135001","val":"Bade"},{"key":"135002","val":"Bursari"},{"key":"135003","val":"Damaturu"},{"key":"135004","val":"Fika"},{"key":"135005","val":"Fune"},{"key":"135006","val":"Geidam"},{"key":"135007","val":"Gujba"},{"key":"135008","val":"Gulani"},{"key":"135009","val":"Jakusko"},{"key":"135010","val":"Karasuwa"},{"key":"135011","val":"Machina"},{"key":"135012","val":"Nangere"},{"key":"135013","val":"Nguru"},{"key":"135014","val":"Potiskum"},{"key":"135015","val":"Tarmuwa"},{"key":"135016","val":"Yunusari"},{"key":"135017","val":"Yusufari"}],"114":[{"key":"114001","val":"Abaji"},{"key":"114002","val":"Bwari"},{"key":"114003","val":"Gwagwalada"},{"key":"114004","val":"Kuje"},{"key":"114005","val":"Kwali"},{"key":"114006","val":"Municipal Area Council"}]},"parentId":"0"}
     */

    private DictMapBean dictMap;

    public DictMapBean getDictMap() {
        return dictMap;
    }

    public void setDictMap(DictMapBean dictMap) {
        this.dictMap = dictMap;
    }

    public static class DictMapBean implements Serializable{
        /**
         * area : {"130":[{"key":"130001","val":"Afijio"},{"key":"130002","val":"Akinyele"},{"key":"130003","val":"Atiba"},{"key":"130004","val":"Atisbo"},{"key":"130005","val":"Egbeda"},{"key":"130006","val":"Ibadan North"},{"key":"130007","val":"Ibadan North-East"},{"key":"130008","val":"Ibadan North-West"},{"key":"130009","val":"Ibadan South-East"},{"key":"130010","val":"Ibadan South-West"},{"key":"130011","val":"Ibarapa Central"},{"key":"130012","val":"Ibarapa East"},{"key":"130013","val":"Ibarapa North"},{"key":"130014","val":"Ido"},{"key":"130015","val":"Irepo"},{"key":"130016","val":"Iseyin"},{"key":"130017","val":"Itesiwaju"},{"key":"130018","val":"Iwajowa"},{"key":"130019","val":"Kajola"},{"key":"130020","val":"Lagelu"},{"key":"130021","val":"Ogbomosho North"},{"key":"130022","val":"Ogbomosho South"},{"key":"130023","val":"Ogo Oluwa"},{"key":"130024","val":"Olorunsogo"},{"key":"130025","val":"Oluyole"},{"key":"130026","val":"Ona Ara"},{"key":"130027","val":"Orelope"},{"key":"130028","val":"Ori Ire"},{"key":"130029","val":"Oyo East"},{"key":"130030","val":"Oyo West"},{"key":"130031","val":"Saki East"},{"key":"130032","val":"Saki West"},{"key":"130033","val":"Surulere"}],"131":[{"key":"131001","val":"Barkin Ladi"},{"key":"131002","val":"Bassa"},{"key":"131003","val":"Bokkos"},{"key":"131004","val":"Jos East"},{"key":"131005","val":"Jos North"},{"key":"131006","val":"Jos South"},{"key":"131007","val":"Kanam"},{"key":"131008","val":"Kanke"},{"key":"131009","val":"Langtang North"},{"key":"131010","val":"Langtang South"},{"key":"131011","val":"Mangu"},{"key":"131012","val":"Mikang"},{"key":"131013","val":"Pankshin"},{"key":"131014","val":"Qua'an Pan"},{"key":"131015","val":"Riyom"},{"key":"131016","val":"Shendam"},{"key":"131017","val":"Wase"}],"110":[{"key":"110001","val":"Abakaliki"},{"key":"110002","val":"Afikpo North"},{"key":"110003","val":"Afikpo South (Edda)"},{"key":"110004","val":"Ebonyi"},{"key":"110005","val":"Ezza North"},{"key":"110006","val":"Ezza South"},{"key":"110007","val":"Ikwo"},{"key":"110008","val":"Ishielu"},{"key":"110009","val":"Ivo"},{"key":"110010","val":"Izzi"},{"key":"110011","val":"Ohaozara"},{"key":"110012","val":"Ohaukwu"},{"key":"110013","val":"Onicha"}],"132":[{"key":"132001","val":"Abua/Odual"},{"key":"132002","val":"Ahoada East"},{"key":"132003","val":"Ahoada West"},{"key":"132004","val":"Akuku-Toru"},{"key":"132005","val":"Andoni"},{"key":"132006","val":"Asari-Toru"},{"key":"132007","val":"Bonny"},{"key":"132008","val":"Degema"},{"key":"132009","val":"Eleme"},{"key":"132010","val":"Emuoha"},{"key":"132011","val":"Etche"},{"key":"132012","val":"Gokana"},{"key":"132013","val":"Ikwerre"},{"key":"132014","val":"Khana"},{"key":"132015","val":"Obio/Akpor"},{"key":"132016","val":"Obio/Akpor"},{"key":"132017","val":"Ogba/Egbema/Ndoni"},{"key":"132018","val":"Ogu/Bolo"},{"key":"132019","val":"Okrika"},{"key":"132020","val":"Omuma"},{"key":"132021","val":"Opobo/Nkoro"},{"key":"132022","val":"Oyigbo"},{"key":"132023","val":"Port Harcourt"},{"key":"132024","val":"Tai"}],"111":[{"key":"111001","val":"Akoko-Edo"},{"key":"111002","val":"Egor"},{"key":"111003","val":"Esan Central"},{"key":"111004","val":"Esan North-East"},{"key":"111005","val":"Esan South-East"},{"key":"111006","val":"Esan West"},{"key":"111007","val":"Etsako Central"},{"key":"111008","val":"Etsako East"},{"key":"111009","val":"Etsako West"},{"key":"111010","val":"Igueben"},{"key":"111011","val":"Ikpoba Okha"},{"key":"111012","val":"Oredo"},{"key":"111013","val":"Orhionmwon"},{"key":"111014","val":"Ovia North-East"},{"key":"111015","val":"Ovia South-West"},{"key":"111016","val":"Owan East"},{"key":"111017","val":"Owan West"},{"key":"111018","val":"Uhunmwonde"}],"133":[{"key":"133001","val":"Binji"},{"key":"133002","val":"Bodinga"},{"key":"133003","val":"Dange Shuni"},{"key":"133004","val":"Gada"},{"key":"133005","val":"Goronyo"},{"key":"133006","val":"Gudu"},{"key":"133007","val":"Gwadabawa"},{"key":"133008","val":"Illela"},{"key":"133009","val":"Isa"},{"key":"133010","val":"Kebbe"},{"key":"133011","val":"Kware"},{"key":"133012","val":"Rabah"},{"key":"133013","val":"Sabon Birni"},{"key":"133014","val":"Shagari"},{"key":"133015","val":"Silame"},{"key":"133016","val":"Sokoto North"},{"key":"133017","val":"Sokoto South"},{"key":"133018","val":"Tambuwal"},{"key":"133019","val":"Tangaza"},{"key":"133020","val":"Tureta"},{"key":"133021","val":"Wamako"},{"key":"133022","val":"Wurno"},{"key":"133023","val":"Yabo"}],"112":[{"key":"112001","val":"Ado Ekiti"},{"key":"112002","val":"Efon"},{"key":"112003","val":"Ekiti East"},{"key":"112004","val":"Ekiti South-West"},{"key":"112005","val":"Ekiti West"},{"key":"112006","val":"Emure"},{"key":"112007","val":"Gbonyin"},{"key":"112008","val":"Ido Osi"},{"key":"112009","val":"Ijero"},{"key":"112010","val":"Ikere"},{"key":"112011","val":"Ikole"},{"key":"112012","val":"Ilejemeje"},{"key":"112013","val":"Irepodun/Ifelodun"},{"key":"112014","val":"Ise/Orun"},{"key":"112015","val":"Moba"},{"key":"112016","val":"Oye"}],"134":[{"key":"134001","val":"Ardo Kola"},{"key":"134002","val":"Bali"},{"key":"134003","val":"Donga"},{"key":"134004","val":"Gashaka"},{"key":"134005","val":"Gassol"},{"key":"134006","val":"Ibi"},{"key":"134007","val":"Jalingo"},{"key":"134008","val":"Karim Lamido"},{"key":"134009","val":"Kumi"},{"key":"134010","val":"Lau"},{"key":"134011","val":"Sardauna"},{"key":"134012","val":"Takum"},{"key":"134013","val":"Ussa"},{"key":"134014","val":"Wukari"},{"key":"134015","val":"Yorro"},{"key":"134016","val":"Zing"}],"113":[{"key":"113001","val":"Aninri"},{"key":"113002","val":"Awgu"},{"key":"113003","val":"Enugu East"},{"key":"113004","val":"Enugu North"},{"key":"113005","val":"Enugu South"},{"key":"113006","val":"Ezeagu"},{"key":"113007","val":"Igbo Etiti"},{"key":"113008","val":"Igbo Eze North"},{"key":"113009","val":"Igbo Eze South"},{"key":"113010","val":"Isi Uzo"},{"key":"113011","val":"Nkanu East"},{"key":"113012","val":"Nkanu West"},{"key":"113013","val":"Nsukka"},{"key":"113014","val":"Oji River"},{"key":"113015","val":"Udenu"},{"key":"113016","val":"Udi"},{"key":"113017","val":"Uzo-Uwani"}],"135":[{"key":"135001","val":"Bade"},{"key":"135002","val":"Bursari"},{"key":"135003","val":"Damaturu"},{"key":"135004","val":"Fika"},{"key":"135005","val":"Fune"},{"key":"135006","val":"Geidam"},{"key":"135007","val":"Gujba"},{"key":"135008","val":"Gulani"},{"key":"135009","val":"Jakusko"},{"key":"135010","val":"Karasuwa"},{"key":"135011","val":"Machina"},{"key":"135012","val":"Nangere"},{"key":"135013","val":"Nguru"},{"key":"135014","val":"Potiskum"},{"key":"135015","val":"Tarmuwa"},{"key":"135016","val":"Yunusari"},{"key":"135017","val":"Yusufari"}],"114":[{"key":"114001","val":"Abaji"},{"key":"114002","val":"Bwari"},{"key":"114003","val":"Gwagwalada"},{"key":"114004","val":"Kuje"},{"key":"114005","val":"Kwali"},{"key":"114006","val":"Municipal Area Council"}]}
         * parentId : 0
         */

        private AreaBean area;
        private List<State> state;
        private String parentId;

        public List<State> getState() {
            return state;
        }

        public void setState(List<State> state) {
            this.state = state;
        }

        public AreaBean getArea() {
            return area;
        }

        public void setArea(AreaBean area) {
            this.area = area;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public static class AreaBean {

            @SerializedName("100")
            private List<keyVaule> _$100;
            @SerializedName("101")
            private List<keyVaule> _$101;
            @SerializedName("102")
            private List<keyVaule> _$102;
            @SerializedName("103")
            private List<keyVaule> _$103;
            @SerializedName("104")
            private List<keyVaule> _$104;
            @SerializedName("105")
            private List<keyVaule> _$105;
            @SerializedName("106")
            private List<keyVaule> _$106;
            @SerializedName("107")
            private List<keyVaule> _$107;
            @SerializedName("108")
            private List<keyVaule> _$108;
            @SerializedName("109")
            private List<keyVaule> _$109;
            @SerializedName("110")
            private List<keyVaule> _$110;

            @SerializedName("111")
            private List<keyVaule> _$111;
            @SerializedName("112")
            private List<keyVaule> _$112;
            @SerializedName("113")
            private List<keyVaule> _$113;
            @SerializedName("114")
            private List<keyVaule> _$114;
            @SerializedName("115")
            private List<keyVaule> _$115;
            @SerializedName("116")
            private List<keyVaule> _$116;
            @SerializedName("117")
            private List<keyVaule> _$117;
            @SerializedName("118")
            private List<keyVaule> _$118;
            @SerializedName("119")
            private List<keyVaule> _$119;
            @SerializedName("120")
            private List<keyVaule> _$120;


            @SerializedName("121")
            private List<keyVaule> _$121;
            @SerializedName("122")
            private List<keyVaule> _$122;
            @SerializedName("123")
            private List<keyVaule> _$123;
            @SerializedName("124")
            private List<keyVaule> _$124;
            @SerializedName("125")
            private List<keyVaule> _$125;
            @SerializedName("126")
            private List<keyVaule> _$126;
            @SerializedName("127")
            private List<keyVaule> _$127;
            @SerializedName("128")
            private List<keyVaule> _$128;
            @SerializedName("129")
            private List<keyVaule> _$129;

            @SerializedName("130")
            private List<keyVaule> _$130;
            @SerializedName("131")
            private List<keyVaule> _$131;
            @SerializedName("132")
            private List<keyVaule> _$132;
            @SerializedName("133")
            private List<keyVaule> _$133;
            @SerializedName("134")
            private List<keyVaule> _$134;
            @SerializedName("135")
            private List<keyVaule> _$135;
            @SerializedName("136")
            private List<keyVaule> _$136;

            public List<keyVaule> get_$100() {
                return _$100;
            }

            public void set_$100(List<keyVaule> _$100) {
                this._$100 = _$100;
            }

            public List<keyVaule> get_$101() {
                return _$101;
            }

            public void set_$101(List<keyVaule> _$101) {
                this._$101 = _$101;
            }

            public List<keyVaule> get_$102() {
                return _$102;
            }

            public void set_$102(List<keyVaule> _$102) {
                this._$102 = _$102;
            }

            public List<keyVaule> get_$103() {
                return _$103;
            }

            public void set_$103(List<keyVaule> _$103) {
                this._$103 = _$103;
            }

            public List<keyVaule> get_$104() {
                return _$104;
            }

            public void set_$104(List<keyVaule> _$104) {
                this._$104 = _$104;
            }

            public List<keyVaule> get_$105() {
                return _$105;
            }

            public void set_$105(List<keyVaule> _$105) {
                this._$105 = _$105;
            }

            public List<keyVaule> get_$106() {
                return _$106;
            }

            public void set_$106(List<keyVaule> _$106) {
                this._$106 = _$106;
            }

            public List<keyVaule> get_$107() {
                return _$107;
            }

            public void set_$107(List<keyVaule> _$107) {
                this._$107 = _$107;
            }

            public List<keyVaule> get_$108() {
                return _$108;
            }

            public void set_$108(List<keyVaule> _$108) {
                this._$108 = _$108;
            }

            public List<keyVaule> get_$109() {
                return _$109;
            }

            public void set_$109(List<keyVaule> _$109) {
                this._$109 = _$109;
            }

            public List<keyVaule> get_$110() {
                return _$110;
            }

            public void set_$110(List<keyVaule> _$110) {
                this._$110 = _$110;
            }

            public List<keyVaule> get_$111() {
                return _$111;
            }

            public void set_$111(List<keyVaule> _$111) {
                this._$111 = _$111;
            }

            public List<keyVaule> get_$112() {
                return _$112;
            }

            public void set_$112(List<keyVaule> _$112) {
                this._$112 = _$112;
            }

            public List<keyVaule> get_$113() {
                return _$113;
            }

            public void set_$113(List<keyVaule> _$113) {
                this._$113 = _$113;
            }

            public List<keyVaule> get_$114() {
                return _$114;
            }

            public void set_$114(List<keyVaule> _$114) {
                this._$114 = _$114;
            }

            public List<keyVaule> get_$115() {
                return _$115;
            }

            public void set_$115(List<keyVaule> _$115) {
                this._$115 = _$115;
            }

            public List<keyVaule> get_$116() {
                return _$116;
            }

            public void set_$116(List<keyVaule> _$116) {
                this._$116 = _$116;
            }

            public List<keyVaule> get_$117() {
                return _$117;
            }

            public void set_$117(List<keyVaule> _$117) {
                this._$117 = _$117;
            }

            public List<keyVaule> get_$118() {
                return _$118;
            }

            public void set_$118(List<keyVaule> _$118) {
                this._$118 = _$118;
            }

            public List<keyVaule> get_$119() {
                return _$119;
            }

            public void set_$119(List<keyVaule> _$119) {
                this._$119 = _$119;
            }

            public List<keyVaule> get_$120() {
                return _$120;
            }

            public void set_$120(List<keyVaule> _$120) {
                this._$120 = _$120;
            }

            public List<keyVaule> get_$121() {
                return _$121;
            }

            public void set_$121(List<keyVaule> _$121) {
                this._$121 = _$121;
            }

            public List<keyVaule> get_$122() {
                return _$122;
            }

            public void set_$122(List<keyVaule> _$122) {
                this._$122 = _$122;
            }

            public List<keyVaule> get_$123() {
                return _$123;
            }

            public void set_$123(List<keyVaule> _$123) {
                this._$123 = _$123;
            }

            public List<keyVaule> get_$124() {
                return _$124;
            }

            public void set_$124(List<keyVaule> _$124) {
                this._$124 = _$124;
            }

            public List<keyVaule> get_$125() {
                return _$125;
            }

            public void set_$125(List<keyVaule> _$125) {
                this._$125 = _$125;
            }

            public List<keyVaule> get_$126() {
                return _$126;
            }

            public void set_$126(List<keyVaule> _$126) {
                this._$126 = _$126;
            }

            public List<keyVaule> get_$127() {
                return _$127;
            }

            public void set_$127(List<keyVaule> _$127) {
                this._$127 = _$127;
            }

            public List<keyVaule> get_$128() {
                return _$128;
            }

            public void set_$128(List<keyVaule> _$128) {
                this._$128 = _$128;
            }

            public List<keyVaule> get_$129() {
                return _$129;
            }

            public void set_$129(List<keyVaule> _$129) {
                this._$129 = _$129;
            }

            public List<keyVaule> get_$130() {
                return _$130;
            }

            public void set_$130(List<keyVaule> _$130) {
                this._$130 = _$130;
            }

            public List<keyVaule> get_$131() {
                return _$131;
            }

            public void set_$131(List<keyVaule> _$131) {
                this._$131 = _$131;
            }

            public List<keyVaule> get_$132() {
                return _$132;
            }

            public void set_$132(List<keyVaule> _$132) {
                this._$132 = _$132;
            }

            public List<keyVaule> get_$133() {
                return _$133;
            }

            public void set_$133(List<keyVaule> _$133) {
                this._$133 = _$133;
            }

            public List<keyVaule> get_$134() {
                return _$134;
            }

            public void set_$134(List<keyVaule> _$134) {
                this._$134 = _$134;
            }

            public List<keyVaule> get_$135() {
                return _$135;
            }

            public void set_$135(List<keyVaule> _$135) {
                this._$135 = _$135;
            }

            public List<keyVaule> get_$136() {
                return _$136;
            }

            public void set_$136(List<keyVaule> _$136) {
                this._$136 = _$136;
            }

            public static class _$100Bean extends keyVaule {
            }

            public static class _$101Bean extends keyVaule {
            }

            public static class _$102Bean extends keyVaule {
            }

            public static class _$103Bean extends keyVaule {
            }

            public static class _$104Bean extends keyVaule {
            }

            public static class _$105Bean extends keyVaule {
            }

            public static class _$106Bean extends keyVaule {
            }

            public static class _$107Bean extends keyVaule {
            }

            public static class _$108Bean extends keyVaule {
            }

            public static class _$109Bean extends keyVaule {
            }

            public static class _$110Bean extends keyVaule {
            }

            public static class _$111Bean extends keyVaule {
            }

            public static class _$112Bean extends keyVaule {
            }

            public static class _$113Bean extends keyVaule {
            }

            public static class _$114Bean extends keyVaule {
            }

            public static class _$115Bean extends keyVaule {
            }

            public static class _$116Bean extends keyVaule {
            }

            public static class _$117Bean extends keyVaule {
            }

            public static class _$118Bean extends keyVaule {
            }

            public static class _$119Bean extends keyVaule {
            }

            public static class _$120Bean extends keyVaule {
            }

            public static class _$121Bean extends keyVaule {
            }

            public static class _$122Bean extends keyVaule {
            }

            public static class _$123Bean extends keyVaule {
            }

            public static class _$124Bean extends keyVaule {
            }

            public static class _$125Bean extends keyVaule {
            }

            public static class _$126Bean extends keyVaule {
            }

            public static class _$127Bean extends keyVaule {
            }

            public static class _$128Bean extends keyVaule {
            }

            public static class _$129Bean extends keyVaule {
            }

            public static class _$130Bean extends keyVaule {
            }

            public static class _$131Bean extends keyVaule {
            }

            public static class _$132Bean extends keyVaule {
            }

            public static class _$133Bean extends keyVaule {
            }

            public static class _$134Bean extends keyVaule {
            }

            public static class _$135Bean extends keyVaule {
            }

            public static class _$136Bean extends keyVaule {
            }

            public static class keyVaule {
                /**
                 * key : 114001
                 * val : Abaji
                 */
                private String key;
                private String val;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public String getVal() {
                    return val;
                }

                public void setVal(String val) {
                    this.val = val;
                }
            }
        }

        public static class State {
            /**
             * key : 110
             * val : Abaji
             */

            private String key;
            private String val;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getVal() {
                return val;
            }

            public void setVal(String val) {
                this.val = val;
            }
        }
    }

}
