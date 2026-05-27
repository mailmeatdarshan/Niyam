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
            subtitle = "Shri Ramachandra Kripalu Bhaju Man",
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
        )
    )
}
