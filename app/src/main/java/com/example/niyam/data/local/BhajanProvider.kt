package com.example.niyam.data.local

import com.example.niyam.R

data class Bhajan(
    val id: String,
    val title: String,
    val subtitle: String,
    val content: String,
    val audioResId: Int? = null
)

object BhajanProvider {
    val bhajans = listOf(
        Bhajan(
            id = "hanuman-chalisa",
            title = "Hanuman Chalisa",
            subtitle = "By Goswami Tulsidas",
            audioResId = R.raw.hanuman_chalisa,
            content = """
                Doha:
                Shri Guru Charan Saroj Raj, Nij Man Mukur Sudhari,
                Barnau Raghuvar Bimal Jasu, Jo Dayaku Phal Chari.
                Buddhiheen Tanu Janike, Sumirau Pavan Kumar,
                Bal Buddhi Vidya Dehu Mohi, Harahu Kalesh Bikaar.

                Chaupai:
                Jai Hanuman Gyan Gun Sagar, Jai Kapis Tihun Lok Ujagar.
                Ram Doot Atulit Bal Dhama, Anjani Putra Pavan Sut Nama.
                Mahabir Bikram Bajrangi, Kumati Nivar Sumati Ke Sangi.
                Kanchan Baran Biraj Subesa, Kanan Kundal Kunchit Kesa.
                Hath Bajra Aur Dhvaja Viraje, Kaandhe Moonj Janeu Saaje.
                Shankar Suvan Kesari Nandan, Tej Pratap Maha Jag Bandan.
                Vidyavan Guni Ati Chatur, Ram Kaj Karibe Ko Atur.
                Prabhu Charitra Sunibe Ko Rasiya, Ram Lakhan Sita Man Basiya.
                Sukshma Roop Dhari Siyahi Dikhava, Bikat Roop Dhari Lanka Jarava.
                Bhim Roop Dhari Asur Sanhare, Ramachandra Ke Kaj Sanvare.
                Laye Sanjivan Lakhan Jiyaye, Shri Raghubir Harashi Ur Laye.
                Raghupati Kinhi Bahut Badai, Tum Mama Priya Bharat Hi Sam Bhai.
                Sahas Badan Tumharo Jas Gave, Asa kahi Shripati Kanth Lagave.
                Sanakadik Brahmadi Munisa, Narad Sarad Sahit Ahisa.
                Yam Kuber Digpal Jahan Te, Kavi Kovid Kahi Sake Kahan Te.
                Tum Upkar Sugreevahi Kinha, Ram Milaye Raj Pad Dinha.
                Tumharo Mantra Vibhishan Maana, Lankeshvar Bhaye Sab Jag Jana.
                Yug Sahastra Yojan Par Bhanu, Lilyo Tahi Madhur Phal Janu.
                Prabhu Mudrika Meli Mukh Mahi, Jaladhi Langhi Gaye Achraj Nahi.
                Durgam Kaj Jagat Ke Jete, Sugam Anugrah Tumhare Tete.
                Ram Duware Tum Rakhavare, Hot Na Agya Binu Paisare.
                Sab Sukh Lahe Tumhari Sarana, Tum Rakshak Kahu Ko Darna.
                Aapan Tej Samharo Ape, Tinon Lok Hank Te Kanpe.
                Bhoot Pisach Nikat Nahi Ave, Mahabir Jab Nam Sunave.
                Nase Rog Hare Sab Pira, Japat Nirantar Hanumat Bira.
                Sankat Te Hanuman Chhudave, Man Kram Bachan Dhyan Jo Lave.
                Sab Par Ram Tapasvee Raja, Tin Ke Kaj Sakal Tum Saja.
                Aur Manorath Jo Koi Lave, Sohi Amit Jivan Phal Pave.
                Charon Yug Partap Tumhara, Hai Parsidh Jagat Ujiyara.
                Sadhu Sant Ke Tum Rakhavare, Asur Nikandan Ram Dulare.
                Ashta Siddhi Nav Nidhi Ke Data, Asabar Din Janki Mata.
                Ram Rasayan Tumhare Pasa, Sada Raho Raghupati Ke Dasa.
                Tumhare Bhajan Ram Ko Pave, Janam Janam Ke Dukh Bisrave.
                Antakal Raghuvar Pur Jai, Jahan Janam Hari Bhakt Kahai.
                Aur Devta Chitt Na Dharayi, Hanumat Sei Sarv Sukh Karayi.
                Sankat Kate Mite Sab Pira, Jo Sumire Hanumat Balbira.
                Jai Jai Jai Hanuman Gosain, Kripa Karahu Gurudev Ki Nyai.
                Jo Sat Bar Path Kar Koi, Chhutahi Bandi Maha Sukh Hoi.
                Jo Yeh Padhe Hanuman Chalisa, Hoye Siddhi Sakhi Gaurisa.
                Tulsidas Sada Hari Chera, Keeje Nath Hriday Mah Dera.

                Doha:
                Pavantanaye Sankat Haran, Mangal Murti Roop.
                Ram Lakhan Sita Sahit, Hriday Basahu Sur Bhoop.
            """.trimIndent()
        ),
        Bhajan(
            id = "ram-stuti",
            title = "Shri Ram Stuti",
            subtitle = "By Avinash Kumar",
            audioResId = R.raw.ram_stuti,
            content = """
                Shri Ramachandra Kripalu Bhaju Man Haran Bhava Bhaya Darunam,
                Navakanja Lochana Kanja Mukha Kara Kanja Pada Kanjarunam.

                Kandarp Agonit Amit Chhavi Nava Nila Nirada Sundaram,
                Pata Pita Manahu Tadita Ruchi Shuchi Naumi Janaka Sutavaram.

                Bhaju Deena Bandhu Dinesh Danava Daitya Vansha Nikandanam,
                Raghunanda Ananda Kanda Kosala Chanda Dasharatha Nandanam.

                Sira Mukuta Kundala Tilaka Charu Udaru Anga Vibhushanam,
                Ajanubahu Shara Chapa Dhara Sangrama Jita Khara Dushanam.

                Iti Vadati Tulasidasa Shankara Shesha Muni Mana Ranjanam,
                Mama Hriday Kanja Nivasa Kuru Kamadi Khala Dala Ganjanam.

                Manu Jahi Rachayu Milihi So Baru Sahaja Sundara Sanvaro,
                Karuna Nidhana Sujanu Sheelu Snehu Janata Ravaro.

                Ehi Banti Gauri Asisa Suni Siya Sahita Hiyali Harishi Ali,
                Tulsi Bhavanihi Puji Puni Puni Mudita Mana Mandira Chali.
            """.trimIndent()
        ),
        Bhajan(
            id = "karpura-gauram",
            title = "Karpura Gauram",
            subtitle = "By Amitabh Bachchan & Kailash Kher",
            audioResId = R.raw.karpurgauram,
            content = """
                Karpura Gauram Karunavataram,
                Sansara Saram Bhujagendra Haram.
                Sada Vasantam Hridayaravinde,
                Bhavam Bhavani Sahitam Namami.

                Meaning:
                Pure white like camphor, the embodiment of compassion,
                The essence of worldly existence, whose garland is the king of serpents.
                Who always dwells in the lotus of the heart,
                I bow to Lord Shiva and Goddess Parvati together.
            """.trimIndent()
        ),
        Bhajan(
            id = "shiv-stotram",
            title = "Shiv Stotram (Tandav)",
            subtitle = "Hymn of Shiva's Cosmic Dance",
            audioResId = R.raw.shiv_stotram,
            content = """
                Jatatavigalajjala pravahapavitasthale
                Galebavalambya lambitam bhujangatungamalikam.
                Damaddamaddamaddaman ninadavadamarvayam
                Chakara chandatandavam tanotu nah shivah shivam.

                Jata katahasambhrama bhramannilimpajhari
                Vilolavichivalarai virajamanamurdhani.
                Dhagadhagadhagajjvalal lalata pattapavake
                Kishorachandrashekhare ratih pratiksanam mama.

                Dharadharendranandini vilasabandhubandhura
                Sphuraddigantasantati pramodamanamanase.
                Krupakataksadhorani nirudhadurdharapadi
                Kwachidigambare mano vinodametu vastuni.
            """.trimIndent()
        ),
        Bhajan(
            id = "gayatri-mantra",
            title = "Gayatri Mantra",
            subtitle = "Sacred Hymn of Rigveda",
            audioResId = R.raw.gayatri_mantra,
            content = """
                Om Bhur Bhuvah Svah
                Tat Savitur Varenyam
                Bhargo Devasya Dheemahi
                Dhiyo Yo Nah Prachodayat.

                Meaning:
                We meditate on the glory of that Creator,
                Who has created the Universe, who is worthy of worship,
                Who is the embodiment of Knowledge and Light,
                Who is the remover of all sins and ignorance.
                May He enlighten our intellect.
            """.trimIndent()
        ),
        Bhajan(
            id = "mahamrityunjaya",
            title = "Mahamrityunjaya Mantra (108)",
            subtitle = "Great Death-Conquering Mantra",
            audioResId = R.raw.maha_mrityunjaya_108,
            content = """
                Om Tryambakam Yajamahe
                Sugandhim Pushti-Vardhanam
                Urvarukamiva Bandhanan
                Mrityor Mukshiya Maamritat.

                Meaning:
                We worship the three-eyed Lord Shiva, who is fragrant and nurtures all beings.
                Just as a ripe cucumber is liberated from its bondage to the vine,
                may He liberate us from death for the sake of immortality, and not separate us from it.
            """.trimIndent()
        ),
        Bhajan(
            id = "humare-saath-shri-raghunath",
            title = "Humare Saath Shri Raghunath",
            subtitle = "By Agam Aggarwal",
            audioResId = R.raw.humare_saath_shri_raghunath,
            content = """
                Humare Saath Shri Raghunath To Kis Baat Ki Chinta,
                Sharan Mein Rakh Diya Jab Maath To Kis Baat Ki Chinta.

                Gaya Lakar Parivaar Humara,
                Karta Hai Voh Hi Palanhaara.
                Haath Mein Jab Hai Unka Haath To Kis Baat Ki Chinta,
                Humare Saath Shri Raghunath To Kis Baat Ki Chinta.

                Aastha Rakho Bhagwaan Mein,
                Bhakti Karo Har Maan Mein.
                Voh Har Pal Hai Tumhare Saath To Kis Baat Ki Chinta,
                Humare Saath Shri Raghunath To Kis Baat Ki Chinta.
            """.trimIndent()
        ),
        Bhajan(
            id = "shankar-teri-jata-me",
            title = "Shankar Teri Jata Se",
            subtitle = "By Pujya Rajan Ji",
            audioResId = R.raw.shankar_teri_jata_me,
            content = """
                Shankar Teri Jata Se Behti Hai Ganga Dhara,
                Kala Ki Shobha Nyari, Bhole Tu Jag Se Pyara.

                Maathe Pe Chand Sohe, Gale Mein Mund Mala,
                Trinetra Dhari Shambho, Piya Zeher Ka Pyala.

                Tum Ho Anaadi Ananta, Devom Ke Mahadeva,
                Karate Hain Dev-Danav Sab Hi Tumhari Seva.
            """.trimIndent()
        ),
        Bhajan(
            id = "tera-mangal-mera-mangal",
            title = "Tera Mangal Mera Mangal",
            subtitle = "Vipassana Meditation & Focus Chant",
            audioResId = R.raw.tera_mangal_mera_mangal,
            content = """
                Tera mangal, mera mangal, sabka mangal hoye re.
                Jis janani ne janam diya hai, us janani ka mangal hoye re.
                Jis pita ne palan kiya hai, us pita ka mangal hoye re.

                Is dharti ke har prani ka, mangal mangal hoye re.
                Dharma ka prachar hoye, shanti ka vistara hoye re,
                Sabka mangal, sabka mangal, sabka mangal hoye re.
            """.trimIndent()
        )
    )
}
