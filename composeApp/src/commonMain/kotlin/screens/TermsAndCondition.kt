package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayopajakmobile.composeapp.generated.resources.placeholder
import ayopajakmobile.composeapp.generated.resources.Res
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.topBar
import org.jetbrains.compose.resources.painterResource

class TermsAndCondition(val email: String): Screen {

	@Composable
	override fun Content() {

		var isChecked by remember { mutableStateOf(false) }

		val navigator = LocalNavigator.currentOrThrow

		Column(
			modifier = Modifier.fillMaxSize()
		) {
			//Header
			topBar("Syarat & Ketentuan")

			//Body
			LazyColumn(
				modifier = Modifier.fillMaxWidth().weight(7.8f).background(Colors().panel)
			) {
				item() {
					Image(
						modifier = Modifier.fillMaxWidth().padding(16.dp),
						painter = painterResource(Res.drawable.placeholder),
						contentDescription = null
					)
				}

				item() {
					Text(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 32.dp),
						text = "Ketentuan Penggunaan AyoPajak",
						fontSize = 16.sp,
						fontWeight = FontWeight.Bold
					)

					//TODO("Style")
					Text(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
						text = "Selamat datang di AyoPajak!\n" +
								"Terima kasih telah menggunakan produk dan layanan (“Layanan”) yang tersedia pada platform melalui situs web https://www.ayopajak.com (“Platform”).\n" +
								"\n" +
								"MOHON ANDA MEMBACA KETENTUAN PENGGUNAAN INI SERTA KEBIJAKAN PRIVASI KAMI DENGAN SEKSAMA SEBELUM MENDAFTARKAN DIRI ANDA ATAU MENGAKSES LAYANAN PADA PLATFORM KAMI.\n" +
								"\n" +
								"Platform ini dioperasikan oleh AyoPajak. Dengan menggunakan Platform ini, Anda mengakui telah membaca dengan seksama, memahami, menerima dan menyetujui seluruh Ketentuan Penggunaan ini. Ketentuan Penggunaan ini adalah perjanjian yang sah dan berlaku antara Anda dan PT. Garda Bina Utama (“GBU” atau “Kami” ).\n" +
								"\n" +
								"Penggunaan Platform dan Layanan AyoPajak\n" +
								"Pembuatan dan Pengelolaan Akun\n" +
								"Anda perlu melengkapi informasi yang Kami minta dan menyetujui Ketentuan Peggunaan ini dan Kebijakan Privasi Kami saat pembuatan akun.\n" +
								"Akun Anda secara otomatis dapat langsung digunakan untuk memperoleh Layanan setelah pendaftaran selesai.\n" +
								"Pembuatan akun AyoPajak (“Akun”) gratis.\n" +
								"Akun Anda hanya dapat digunakan oleh Anda dan tidak bisa dialihkan kepada orang lain dengan alasan apapun.\n" +
								"Kami tidak mengetahui dan tidak akan meminta username dan kata sandi Anda (“Informasi Akun”) untuk alasan apapun. Jagalah kerahasiaan data Anda tersebut dan jangan memberitahukannya kepada siapapun termasuk Kami atau pihak-pihak yang mengatasnamakan/mengaku sebagai perwakilan Kami. Dalam hal Informasi Akun Anda menjadi diketahui oleh pihak lain yang diakibatkan oleh kelalaian Anda sendiri atau alasan apapun, perintah yang diterima dari penggunaan oleh pihak ketiga tanpa otorisasi tersebut akan dianggap sebagai perintah Anda, kecuali Anda telah memberitahu Kami untuk memblokir Akun sebelum perintah tersebut diterima oleh Kami. Anda setuju untuk bertanggung jawab penuh terhadap setiap aktivitas pada akun Anda dan konsekuensi yang timbul daripadanya. Anda membebaskan Kami dari klaim atau tuntutan hukum apapun yang muncul jika terjadi penyalahgunaan Layanan menggunakan akun Anda.\n" +
								"Atas penilaian dan diskresi Kami sendiri, Kami berhak melakukan pemblokiran akun sementara atau permanen (“Pemblokiran Akun”) apabila Kami mengetahui bahwa terdapat penyalahgunaan akun atau penggunaan akun untuk tujuan yang bertentangan dengan Ketentuan Penggunaan ini atau ketentuan hukum yang berlaku (“Aktivitas Tidak Sah”). Untuk Aktivitas Tidak Sah berupa pelanggaran terhadap ketentuan hukum yang berlaku, maka Kami akan melakukan upaya-upaya hukum yang dianggap perlu sesuai dengan ketentuan peraturan perundang-undangan yang berlaku.\n" +
								"Penggunaan Platform dan Layanan\n" +
								"Anda dapat mengakses Platform dan/atau memilih dan menggunakan Layanan setelah Anda memiliki akun.\n" +
								"Kami menyediakan Layanan One Stop Services antara lain dibidang perpajakan. Kami juga memfasilitasi Anda untuk dapat memperoleh beragam layanan yang Anda perlukan dan meghubungkan Anda dengan pihak ketiga penyedia layanan yang bekerjasama dengan Kami (“Partner”).\n" +
								"Anda mengerti dan setuju bahwa tiap-tiap layanan yang tersedia di Platform dapat memiliki syarat dan ketentuan tersendiri sehingga Anda wajib tunduk pada masing-masing ketentuan layanan sebelum dapat menggunakan layanan terkait. Anda mengerti bahwa Kami dapat melakukan perubahan atau pemutakhiran terhadap Ketentuan Penggunaan ini, DENGAN TETAP MELANJUTKAN AKSES KE PLATFORM ATAU PENGGUNAAN LAYANAN, ANDA SETUJU UNTUK TUNDUK PADA SELURUH KETENTUAN PENGGUNAAN INI TERMASUK SETIAP PERUBAHANNYA DAN KETENTUAN PENGGUNAAN DARI SETIAP PARTNER. Hentikan akses ke Platform atau penggunaan Layanan jika Anda tidak setuju dengan bagian apapun dari Ketentuan Penggunaan ini.\n" +
								"Anda dengan ini mengakui dan setuju bahwa segala hubungan hukum terkait dengan layanan yang disediakan oleh Partner Kami (“Layanan Partner”) adalah antara Anda dengan Partner terkait. Dengan demikian segala hal sehubungan dengan kualitas produk dan/atau layanan merupakan tanggung jawab dari Partner.\n" +
								"Setiap aktivitas yang Anda lakukan pada Platform memiliki rekam jejak/audit trail. Anda akan memperoleh notifikasi pada akun Anda dan/atau melalui email untuk setiap aktivitas pada Platform.\n" +
								"Kebijakan Privasi AyoPajak\n" +
								"Dengan menggunakan Platform dan Layanan Kami, Anda memercayakan data dan Informasi Anda kepada Kami. Dengan menyetujui Ketentuan Penggunaan ini, Anda menyatakan bahwa Anda telah membaca dengan seksama dan menyetujui Kebijakan Privasi Kami yang merupakan bagian yang tidak terpisahkan dari Ketentuan Penggunaan ini.\n" +
								"Pernyataan dan Jaminan Anda\n" +
								"Anda menyatakan dan menjamin bahwa:\n" +
								"Anda adalah individu yang secara hukum cakap melakukan tindakan hukum, yakni Anda yang telah berusia minimal 18 (delapan belas) tahun atau tidak dalam pengampuan. Dalam hal Anda bertindak untuk dan atas nama atau mewakili suatu badan, maka Anda juga menjamin bahwa Anda memiliki kapasitas hukum yang cukup berdasarkan suatu kuasa dan/atau penunjukkan yang sah.\n" +
								"Kami berhak untuk meminta data, Informasi, dokumen, dan keterangan pendukung sewaktu-waktu jika diperlukan, baik secara elektronik atau secara langsung ke alamat kantor Kami sebagaimana Kami instruksikan.\n" +
								"Dengan menyetujui Ketentuan Penggunaan ini, Anda menyatakan bahwa seluruh data yang telah Anda sampaikan pada saat pembuatan Akun, di kemudian hari atau dari waktu ke waktu adalah benar, akurat, dan lengkap.\n" +
								"Konten\n" +
								"Setiap Konten yang terdapat pada Platform dan/atau Layanan adalah milik Kami atau Partner.\n" +
								"Dalam hal Konten tersebut dimiliki oleh Partner, maka sebagian maupun seluruh isi Konten merupakan tanggung jawab Partner. Anda perlu menyetujui ketentuan penggunaan sebelum dapat menggunakan layanan Partner.\n" +
								"Kami dan/atau Partner, dari waktu ke waktu, dapat mengubah Konten tanpa perlu menyampaikan pemberitahuan atau notifikasi pada Platform dan/atau surat elektronik kepada Anda terlebih dahulu, kecuali terkait penawaran layanan dari Partner baru Kami (“Penawaran Layanan”).\n" +
								"Anda diberikan kebebasan untuk memilih menggunakan atau tidak menggunakan Penawaran Layanan.\n" +
								"Biaya, Subscription Services dan Pembayaran\n" +
								"Anda dapat tidak dikenakan biaya apapun untuk penggunaan layanan Freemium atau jenis layanan sebagaimana Kami informasikan dari waktu ke waktu di Platform.\n" +
								"Anda akan dikenakan biaya berlangganan untuk penggunaan Layanan berdasarkan klasifikasi atau paket-paket layanan (“Paket Layanan”) yang Kami sediakan dan informasikan di Platform. Apabila Anda mengubah pilihan Paket Layanan Anda di Platform, maka Anda setuju untuk mengubah biaya berlangganan sebagaimana Kami informasikan dari waktu ke waktu melalui Platform.\n" +
								"Anda wajib melakukan pembayaran atas tagihan yang dikirimkan secara elektronik kepada Anda (“Tagihan”) pada Tanggal Jatuh Tempo Tagihan.\n" +
								"Anda dapat dikenai Denda Keterlambatan apabila Anda lalai membayar jumlah Tagihan yang telah jatuh tempo. Denda Keterlambatan tersebut dimulai setelah lewat Tanggal Jatuh Tempo dan akan diakumulasikan sampai dengan pembayaran atas seluruh jumlah Tagihan oleh Anda. Besarnya jumlah denda keterlambatan akan diinformasikan dari waktu ke waktu di Platform.\n" +
								"Pembayaran Tagihan dapat dilakukan dengan metode transfer bank ke rekening Kami atau melalui PT. Garda Bina Utama (“GBU”) atau metode pembayaran lainnya (“Operator Pembayaran”) sebagaimana tersedia di Platform. Apabilametode pembayaran lainnya, maka Anda tunduk pada ketentuan metode pembayaran lainnya tersebut. Segala biaya-biaya administrasi seperti biaya transfer yang timbul pada saat Pembayaran Tagihan adalah menjadi tanggung jawab Anda.\n" +
								"Anda dapat melakukan pembayaran Tagihan lebih cepat dari Tanggal Jatuh Tempo sesuai dengan besaran seluruh jumlah Tagihan yang Anda terima.\n" +
								"Hak dan Kewajiban Anda\n" +
								"Dengan tidak mengesampingkan hak-hak dan kewajiban-kewajiban lain yang diatur dalam Ketentuan Penggunaan ini, hak dan kewajiban Anda adalah sebagai berikut:\n" +
								"Berhak menggunakan layanan sesuai dengan Paket Layanan yang Anda pilih;\n" +
								"Berhak melakukan pembayaran Tagihan dipercepat sesuai dengan ketentuan dalam Penggunaan ini; dan\n" +
								"Wajib melakukan pembayaran secara penuh atas Tagihan yang timbul sebagai akibat dari penggunaan Layanan dan/atau biaya-biaya lain berdasarkan Ketentuan Penggunaan ini.\n" +
								"Hak dan Kewajiban Kami\n" +
								"Dengan tidak mengesampingkan hak-hak dan kewajiban-kewajiban lain yang diatur dalam Ketentuan Penggunaan ini, hak dan kewajiban Kami adalah sebagai berikut:\n" +
								"Berhak menerima pembayaran secara penuh atas seluruh kewajiban pembayaran Anda yang timbul sebagai akibat dari penggunaan Layanan atau biaya-biaya lainnya berdasarkan Ketentuan Penggunaan ini; dan\n" +
								"Wajib menyediakan Layanan berdasarkan Ketentuan Penggunaan ini.\n" +
								"Hak Kekayaan Intelektual\n" +
								"Platform dan Layanan, termasuk namun tidak terbatas pada nama, desain logo, grafis, kata-kata, angka-angka, model dan proses bisnis, Konten dilindungi oleh hak kekayaan intelektual berdasarkan hukum Republik Indonesia baik yang terdaftar atas nama Kami atau afiliasi Kami.\n" +
								"Anda tidak diperkenankan untuk mengadaptasi, menyalin, memodifikasi mendistribusikan, menerjemahkan, membuat karya turunan dari, memberikan lisensi, menjual, mengalihkan, menampilkan di muka umum, menyiarkan, menguraikan atau membongkar bagian manapun dengan cara mengeksploitasi Platform beserta seluruh jaringan sistemnya antara lain perangkat lunak.\n" +
								"Anda tidak diperbolehkan untuk memberikan lisensi, mensublisensikan, menjual, memindahkan, mengalihkan, mendistribusikan atau mengeksploitasi secara komersial atau membuat tersedia kepada pihak lain dengan cara mirror jaringan sistem perangkat lunak pada server lain atau perangkat nirkabel atau yang berbasis internet.\n" +
								"Anda dilarang menerbitkan, mendistribusikan atau memperbanyak dengan cara apapun materi yang dilindungi hak kekayaan intelektual tanpa persetujuan tertulis terlebih dahulu dari Kami atau pemberi lisensi Kami.\n" +
								"Batasan Tanggung Jawab Kami\n" +
								"Platform menyediakan Layanan sebagaimana adanya “as is” dan Kami tidak menyatakan atau menjamin bahwa keandalan, ketepatan waktu, kualitas, kesesuaian, ketersediaan, dan/atau akurasi kelengkapan dan/atau ketersediaan Layanan dapat memenuhi kebutuhan dan akan sesuai dengan harapan/ekspetasi Anda. Kami tidak bertanggung jawab terhadap atas setiap kerugian atau kerusakan yang disebabkan oleh setiap kegagalan atau kesalahan pada Layanan Partner, Konten yang disediakan oleh Partner, dan/atau yang dilakukan oleh Operator Pembayaran atau kesalahan Anda dalam mematuhi Ketentuan Penggunaan ini.\n" +
								"Sistem Kami dapat mengalami keterbatasan, penundaan, dan masalah-masalah yang disebabkan oleh gangguan pada sistem Direktorat Jenderal Pajak, masalah-masalah yang terdapat dalam penggunaan internet dan komunikasi elektronik. Kami tidak bertanggung jawab atas keterlambatan, kegagalan pengiriman, kerusakan atau kerugian yang diakibatkan masalah-masalah diluar kendali Kami.\n" +
								"Cidera Janji\n" +
								"Peristiwa Cidera Janji timbul apabila salah satu atau lebih dari kejadian-kejadian berikut ini terjadi:\n" +
								"Anda, karena sebab apapun, telah gagal memenuhi kewajiban pembayaran Anda berdasarkan Ketentuan Penggunaan ini;\n" +
								"Penggunaan Akun Anda dan/atau Layanan dengan cara yang bertentangan dengan Ketentuan Penggunaan ini, ketentuan penggunaan terkait yang terdapat pada Platform, Kebijakan Privasi Kami dan/atau setiap ketentuan peraturan perundang-undangan yang berlaku;dan/atau\n" +
								"Pernyataan-pernyataan dan/atau jaminan-jaminan Anda dalam ketentuan Penggunaan ini tidak benar atau menjadi tidak benar.\n" +
								"Atas Peristiwa Cidera Janji sebagaimana disebutkan dalam Pasal 10.1 di atas, Kami berhak untuk:\n" +
								"menghentikan Layanan dan/atau melakukan pemblokiran Akun secara sepihak berdasarkan Ketentuan Penggunaan ini;\n" +
								"menyatakan bahwa sebagian atau seluruh jumlah kewajiban pembayaran Tagihan yang masih terutang menjadi jatuh tempo dan wajib Anda bayar seketika; dan/atau\n" +
								"melakukan tindakan-tindakan lainnya guna mendapatkan pembayaran sebagaimana dimaksud dalam huruf (b) Pasal ini.\n" +
								"Para Pihak dengan ini setuju untuk mengesampingkan ketentuan-ketentuan dalam Pasal 1266 dan 1267 Kitab Undang-undang Hukum Perdata sejauh ketentuan-ketentuan tersebut membutuhkan persetujuan atau keputusan pengadilan untuk mengakhiri Perjanjian ini.\n" +
								"Keadaan Kahar\n" +
								"Dalam hal Platform mengalami gangguan-gangguan yang disebabkan oleh kejadian diluar kewenangan atau kontrol Kami (“Keadaan Kahar”) antara lain bencana alam, gangguan iklim ekstrim, gangguan telekomunikasi, gangguan listrik, perubahan terhadap kebijakan pemerintah, permintaan dari otoritas yang berwenang, dan/atau perubahan peraturan perundang-undangan, Anda setuju untuk membebaskan Kami dari setiap tuntutan dan tanggung jawab atas tidak terlaksananya atau dijalankannya instruksi Anda melalui Platform dikarenakan oleh Keadaan Kahar tersebut.\n" +
								"Hukum Yang Mengatur dan Penyelesaian Sengketa\n" +
								"Ketentuan Penggunaan ini dan pelaksanaannya diatur oleh dan ditafsirkan berdasarkan hukum Negara Republik Indonesia. Setiap dan semua sengketa yang timbul dari penggunaan Layanan dan Ketentuan Penggunaan ini tunduk pada yurisdiksi eksklusif dari Pengadilan Negeri Jakata Barat.\n" +
								"Lain-lain\n" +
								"Definisi\n" +
								"Kecuali secara tegas dinyatakan lain, semua istilah yang didefinisikan dalam Ketentuan Penggunaan ini mempunyai pengertian sebagai berikut:\n" +
								"“Anda” berarti calon pengguna atau pengguna Platform yang akan atau sedang menggunakan Layanan.\n" +
								"“Pribadi” berarti penggunaan Akun untuk diri sendiri atau hanya bisa untuk 1 (satu) NPWP.\n" +
								"“Konsultan” berarti penggunaan Akun untuk mengatur lebih dari 1 (satu) NPWP.\n" +
								"“Tanggal Jatuh Tempo” berarti tanggal penagihan sebagaimana tertera pada Tagihan.\n" +
								"“AyoPajak” berarti PT. Garda Bina Utama dan/atau afiliasinya.\n" +
								"“Informasi” berarti segala informasi, keterangan, dan data, baik yang ada saat ini ataupun di masa mendatang termasuk segala perubahannya dan/atau pemutakhirannya, mengenai diri Anda yang Anda berikan kepada Kami dana/atau Partner serta informasi yang dapat diidentifikasi dan dikumpulkan melalui Platform dan penggunaan setiap layanan yang tersedia pada Platform.\n" +
								"“Hari Kalender” berarti hari Senin sampai dengan hari Minggu, termasuk hari libur nasional dan cuti bersama.\n" +
								"“Hari Kerja” berarti hari, selain hari Sabtu, Minggu dan hari libur resmi nasional, dimana bank buka untuk melakukan kegiatan usahanya sesuai dengan ketentuan Bank Indonesia.\n" +
								"Apabila satu atau lebih ketentuan dalam Ketentuan Penggunaan ini menjadi tidak berlaku, tidak sah atau tidak dapat dilaksanakan dengan cara apapun menurut hukum yang berlaku, maka hal tersebut tidak akan mempengaruhi keabsahan, keberlakukan, dan dapat dilaksanakannya ketentuan-ketentuan lain dalam Ketentuan Penggunaan ini.\n" +
								"Ketentuan Penggunaan ini dibuat bilingual dalam Bahasa Indonesia dan Bahasa Inggris. Dalam hal terjadi ketidaksesuaian antara versi Bahasa Indonesia dan Bahasa Inggris, versi Bahasa Indonesia akan berlaku.\n" +
								"Hubungi Kami\n" +
								"Anda dapat menghubungi Kami melalui surat elektronik ke support@ayopajak.com atau melalui live chat kami pada Platform. Seluruh korespondensi dan percakapan akan direkam dan disimpan untuk arsip Kami.",
						fontSize = 12.sp,
					)
				}

				item() {
					Row(
						modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Checkbox(
							checked = isChecked,
							onCheckedChange = { isChecked = it },
							colors = CheckboxDefaults.colors(
								checkedColor = Colors().brandDark60
							)
						)
						Text(
							modifier = Modifier.fillMaxWidth().padding(end = 16.dp)
								.clickable(onClick = { isChecked = !isChecked }),
							text = "Dengan ini saya membaca, memahami, dan menyetujui hal-hal yang tercantum pada syarat dan ketentuan yang berlaku",
							fontSize = 12.sp,
							fontWeight = FontWeight.Bold
						)
					}

					Button(
						modifier = Modifier.height(48.dp).fillMaxWidth().padding(horizontal = 16.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							navigator.push(PrivacyPolicy(email))
						},
						enabled = isChecked
					) {
						Text(
							text = "Berikutnya",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold
						)
					}
				}
			}

			//Footer
			Column(
				modifier = Modifier.fillMaxWidth().weight(1f).background(Colors().panel),
				verticalArrangement = Arrangement.Bottom
			) {
				Text(
					modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
					text = "Baca hingga akhir untuk melanjutkan",
					fontSize = 10.sp,
					color = Colors().textDarkGrey
				)
			}
		}
	}
}