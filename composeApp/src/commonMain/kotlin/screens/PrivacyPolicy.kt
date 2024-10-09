package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.placeholder
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.topBar
import org.jetbrains.compose.resources.painterResource

class PrivacyPolicy(val email: String): Screen {

	@Composable
	override fun Content() {

		var isChecked by remember { mutableStateOf(false) }

		val navigator = LocalNavigator.currentOrThrow

		Column(
			modifier = Modifier.fillMaxSize()
		) {
			//Header
			topBar("Kebijakan Privasi")

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
						text = "Kebijakan Privasi",
						fontSize = 16.sp,
						fontWeight = FontWeight.Bold
					)

					//TODO("Style")
					Text(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
						text = "Undang-Undang No. 11 Tahun 2008 tentang Informasi dan Transaksi Elektronik, sebagaimana telah diubah dengan Undang-Undang No. 19 Tahun 2016 tentang Perubahan atas Undang-Undang No. 11 Tahun 2008 tentang Informasi dan Transaksi Elektronik serta seluruh peraturan pelaksananya (“UU ITE”) diperkenalkan oleh Pemerintah Republik Indonesia untuk mengatur transaksi-transaksi elektronik, termasuk pengolahan data pribadi. UU ITE, yang berlaku untuk semua perusahaan dan badan usaha yang didirikan di Indonesia, mengharuskan kami untuk memberitahu pengguna kami (“Pengguna”, “Anda”) tentang hak-hak Anda sehubungan dengan data pribadi Anda yang sedang diproses atau yang akan dikumpulkan dan diproses lebih lanjut oleh kami serta tujuan dari pengolahan data Anda. UU ITE ini juga mengharuskan kami untuk mendapatkan persetujuan Anda atas pengolahan data pribadi Anda.\n" +
								"Garda Bina Utama atau salah satu perusahaan afiliasinya (“GBU”, “kami”, atau “milik kami”) mengoperasikan dan menyediakan layanan online dalam situs ini, termasuk namun tidak terbatas pada layanan aplikasi AyoPajak, layanan e-Faktur, serta layanan, konten, update, perangkat lunak lainnya dan rilis baru yang mungkin kami sediakan dari waktu ke waktu (“Layanan”) dan kami berkomitmen untuk melindungi data pribadi Anda. Pernyataan Privasi ini menjelaskan bagaimana kami mengumpulkan dan mengolah data Anda. Kami memiliki komitmen untuk tidak akan menjual, mempublikasikan atau membagikan informasi pribadi Anda maupun orang lain demi menghargai kepercayaan yang telah Anda taruh kepada kami sesuai dengan hukum yang berlaku.\n" +
								"Pernyataan Privasi ini berlaku untuk seluruh kegiatan usaha kami di Indonesia.\n" +
								" \n" +
								"Jenis informasi apa saja yang kami kumpulkan dari Anda?\n" +
								"Jenis informasi yang kami kumpulkan ketika berinteraksi dengan Anda sehubungan dengan Layanan, [seperti penjualan (sales), layanan jasa, bantuan, pendaftaran dan pembayaran], yakni mencakup:\n" +
								"Nama;\n" +
								"Alamat pengiriman atau penagihan;\n" +
								"Surat elektronik (e-mail);\n" +
								"Nomor telepon;\n" +
								"Nama Pengguna dan kata sandi Anda untuk mengakses produk dan Layanan kami;\n" +
								"Sertifikat Digital;\n" +
								"Passphrase;\n" +
								"Data pelanggan atau data perusahaan yang telah Anda simpan dan gunakan dengan kami yang terkait dengan pelaporan dan pembayaran pajak perusahaan;\n" +
								"Informasi mengenai sistem Anda seperti pada saat berinteraksi dengan kami, misalnya alamat IP dan informasi browser Anda; dan\n" +
								"Saran dari Pengguna, diskusi-diskusi komunitas (masyarakat), obrolan-obrolan dan kegiatan interaksi lainnya dalam situs kami.\n" +
								"(Seluruh jenis informasi di atas selanjutnya disebut sebagai “Informasi Pribadi”)\n" +
								" \n" +
								"Apa saja yang kami lakukan dengan Informasi Pribadi Anda?\n" +
								"Informasi Pribadi dan informasi lainnya yang Anda berikan dan jika relevan, untuk penggunaan, atau berlangganan, atau pembelian Layanan dan/atau produk kami, termasuk informasi tambahan yang selanjutnya Anda berikan, dapat digunakan dan diolah oleh kami untuk tujuan berikut:\n" +
								"Menggunakan informasi Anda untuk menjalankan usaha kami dan membantu kami dalam meningkatkan pengalaman anda dengan produk kami;\n" +
								"Memberitahu Anda mengenai produk dan layanan jasa yang tersedia untuk Anda;\n" +
								"Memberikan beberapa pilihan mengenai kegunaan informasi kami yang sesuai dengan Anda;\n" +
								"Memberikan penjelasan yang terbuka dan jelas mengenai bagaimana kami menggunakan informasi tersebut;\n" +
								"Mempublikasikan atau membagikan informasi yang telah dikombinasi dari beberapa pengguna, namun tentu saja dengan cara tidak akan membiarkan Anda atau orang lain teridentifikasi;\n" +
								"Mengagregasikan data akun Anda, yang sudah diunggah dan non-personal sehingga Anda tidak dapat diidentifikasi, dengan data milik pengguna lain Layanan untuk meningkatkan kualitas pelayanan, merancang promosi atau memberikan cara bagi Anda untuk membandingkan praktek bisnis dengan pengguna lainnya;\n" +
								"Melatih karyawan kami dan juga melatih Anda tentang bagaimana menjaga informasi Anda agar tetap aman dan terlindungi;\n" +
								"Untuk memperoleh dan mengumpulkan Informasi Pribadi Anda, serta menyimpan Informasi Pribadi Anda dalam suatu sistem elektronik yang dimiliki oleh GBU atau pihak ketiga;\n" +
								"Menilai dan memproses permintaan Pengguna terkait Layanan;\n" +
								"Menetapkan identitas dan latar belakang Pengguna;\n" +
								"Membangun komunikasi antara Pengguna dan GBU;\n" +
								"Menanggapi pertanyaan, keluhan atau komentar dari Pengguna;\n" +
								"Mengelola partisipasi Pengguna dalam acara atau program yang diselenggarakan GBU;\n" +
								"Mengolah dan menganalisis Informasi Pribadi Anda, termasuk untuk melaksanakan analisis pasar, baik yang dilakukan oleh GBU atau pihak ketiga;\n" +
								"Menampilkan, mengumumkan dan membuka akses Informasi Pribadi Anda kepada anak perusahaan, afiliasi, perusahaan terkait, pemegang lisensi, mitra usaha dan/atau penyedia layanan GBU;\n" +
								"Melakukan kegiatan internal, termasuk investigasi internal, kepatuhan, audit dan keperluan keamanan internal lainnya; dan\n" +
								"Kegiatan usaha sah lainnya dari GBU.\n" +
								"(secara kolektif “Tujuan“)\n" +
								" \n" +
								"Apakah kami memindahkan atau mengungkapkan Informasi Pribadi Anda ke pihak ketiga?\n" +
								"GBU dapat mengakses atau menyimpan Informasi Pribadi. Anda memahami dan setuju bahwa Informasi Pribadi anda dapat dipindahkan, disimpan, digunakan, diolah dan diproses dimana server-server kami berada.\n" +
								"Anda memahami dan menyetujui bahwa kami mungkin memberikan dan/atau mengungkapkan Informasi Pribadi Anda dengan cara-cara yang terbatas:\n" +
								"Guna memungkinkan kami melakukan Tujuan sebagaimana dijelaskan di atas, kami mungkin memberikan dan/atau mengungkapkan Informasi Pribadi Anda kepada anak perusahaan, afiliasi, perusahaan terkait, pemegang lisensi, mitra usaha, penyedia layanan kami, penasihat profesional dan auditor eksternal kami, termasuk penasihat hukum, penasihat keuangan dan konsultan-konsultan, serta pihak ketiga lainnya, yang mungkin terletak di dalam atau di luar Indonesia.\n" +
								"Kami mungkin menyediakan fitur layanan yang menghubungkan Anda dengan mitra usaha, penyedia layanan atau pihak ketiga lainnya, dan dengan demikian kami dapat mengungkapkan beberapa Informasi Pribadi Anda secara terbatas ke mitra usaha, penyedia layanan atau pihak ketiga lainnya tersebut hanya untuk keperluan pelaksanaan fitur layanan tersebut.\n" +
								"Kami mungkin terlibat atau mempekerjakan perusahaan atau perseorangan lain untuk memfasilitasi, memberikan layanan-layanan tertentu atau melakukan fungsi atas nama kami, dan sehubungan dengan hal tersebut kami mungkin memberikan dan/atau mengungkapkan Informasi Pribadi Anda kepada perusahaan atau perseorangan lain tersebut.\n" +
								"Apabila terjadi suatu transaksi perusahaan, termasuk namun tidak terbatas pada, penjualan anak perusahaan atau divisi, peleburan, konsolidasi, pembiayaan, penjualan aset atau situasi lain apapun yang melibatkan pemindahan sebagian atau seluruh aset bisnis kami, kami mungkin mengungkapkan Informasi Pribadi Anda kepada pihak-pihak yang terlibat di dalam perundingan atau pemindahan tersebut.\n" +
								"Kami mungkin juga mengungkapkan Informasi Pribadi Anda apabila diwajibkan secara hukum, atau diperlukan untuk tunduk pada ketentuan peraturan perundang-undangan, peraturan-peraturan dan pemerintah, atau dalam hal terjadi sengketa, atau segala bentuk proses hukum terkait dengan Layanan, atau dalam keadaan darurat yang berkaitan dengan kesehatan dan/atau keselamatan Anda.\n" +
								"Atas perintah aparat penegak hukum atau instansi pemerintah yang berwenang berdasarkan ketentuan peraturan perundang-undangan yang berlaku, kami dapat memberikan akses kepada aparat penegak hukum atau instansi pemerintah yang bersangkutan untuk melaksanakan penggeledahan atau penyitaan atas data Anda yang disimpan secara elektronik di dalam server kami.\n" +
								"Kami mungkin juga memberikan informasi himpunan atau anonim yang tidak secara langsung mengidentifikasikan Anda.\n" +
								"Selain untuk hal tersebut di atas, Informasi Pribadi Anda tidak akan secara sengaja ditransfer ke setiap tempat di luar Indonesia atau akan secara sengaja diungkapkan kepada pihak ketiga.\n" +
								" \n" +
								"Bagaimana kami menjaga keamanan Informasi Pribadi Anda?\n" +
								"Dalam menjaga keamaan Informasi Pribadi Anda, kami telah:\n" +
								"Menggunakan metode-metode terbaik yang telah teruji untuk melindungi informasi Anda;\n" +
								"Meninjau kembali prosedur keamanan kami secara hati-hati;\n" +
								"Mematuhi hukum dan standar keamanan yang berlaku;\n" +
								"Memastikan bahwa Informasi Pribadi Anda terkirim dengan aman; and\n" +
								"Memastikan bahwa karyawan kami sudah dilatih dan diwajibkan untuk ikut mengamankan informasi Anda.\n" +
								"Kami akan mengambil langkah-langkah yang diperlukan untuk mempertahankan privasi dan keamanan dari seluruh Informasi Pribadi yang Anda berikan. Kami akan memberitahu Anda jika pihak ketiga mana pun (seperti peretas) menembus atau telah berusaha untuk menembus langkah-langkah keamanan kami atau dengan tidak sah mendapatkan akses ke pusat data atau perangkat kami yang berisi Informasi Pribadi Anda. Dalam hal terjadi peretasan yang berada di luar kendali kami, Anda setuju untuk membebaskan GBU dari seluruh klaim, tanggung jawab hukum ataupun pengeluaran apapun yang muncul dari peretasan tersebut.\n" +
								" \n" +
								"Penyimpanan Informasi Pribadi Anda\n" +
								"Kami menyimpan Informasi Pribadi Anda dalam catatan bisnis kami selama Anda menjadi pelanggan atau pengguna dari salah satu produk dan/atau Layanan kami atau sebagai pengguna situs kami. Kami juga menyimpan Informasi Pribadi Anda untuk jangka waktu tertentu setelah Anda tidak lagi menjadi pelanggan atau pengguna dari salah satu produk dan/atau Layanan kami atau sebagai pengguna situs kami jika Informasi Pribadi diperlukan untuk Tujuan yang oleh karenanya Informasi Pribadi dikumpulkan atau untuk memenuhi persyaratan hukum.\n" +
								" \n" +
								"Penarikan Kembali Persetujuan\n" +
								"Anda dapat menarik kembali persetujuan yang telah Anda berikan atas Informasi Pribadi Anda dengan mengajukan permohonan tertulis kepada GBU ke halaman web.\n" +
								"Setelah menerima permohonan tertulis tersebut, kami akan memberitahu Anda mengenai konsekuensi yang mungkin terjadi sehubungan dengan penarikan kembali persetujuan Anda, dan apabila Anda tetap bermaksud untuk menarik kembali persetujuan Anda, Anda dengan ini menerima dan melepaskan GBU dari segala konsekuensi hukum yang timbul dari penarikan kembali tersebut.\n" +
								" \n" +
								"Akses\n" +
								"GBU akan, pada saat Anda memilih untuk tidak lagi menggunakan layanan e-Faktur GBU, mengizinkan Anda untuk melihat Informasi Pribadi Anda yang tersimpan.\n" +
								"Situasi tertentu termasuk (sepanjang diperbolehkan berdasarkan hukum yang berlaku) ketika:\n" +
								"(a) Otoritas yang sedang melakukan investigasi atau lembaga pemerintahan tidak setuju GBU memenuhi permintaan Anda; dan/atau\n" +
								"(b) Informasi dikumpulkan sehubungan dengan suatu investigasi atas suatu pelanggaran kontrak, dugaan terjadinya kegiatan curang atau perbuatan melawan hukum.\n" +
								"GBU juga tidak diwajibkan untuk memberikan akses terhadap Informasi Pribadi Anda apabila diperkirakan secara wajar dapat:\n" +
								"(a) mengancam atau membahayakan secara langsung atau serius keamanan atau kesehatan fisik atau mental seseorang selain dari Anda;\n" +
								"(b) mengungkapkan data pribadi orang lain;\n" +
								"(c) mengungkapkan identitas seseorang yang telah memberikan data pribadi orang lain dan orang yang memberikan data pribadi tersebut tidak menyetujui pengungkapan identitasnya; atau\n" +
								"(d) bertentangan dengan kepentingan nasional;\n" +
								" \n" +
								"Ketepatan dan Perbaikan\n" +
								"GBU membutuhkan bantuan dari Anda untuk memastikan bahwa Informasi Pribadi Anda adalah terbaru, lengkap dan akurat. Sehubungan dengan hal tersebut, kami mohon agar Anda dapat memberitahu GBU atas perubahan-perubahan terhadap Informasi Pribadi Anda atau apabila Anda bermaksud untuk meminta perbaikan terhadap Informasi Pribadi Anda dengan mengirimkan pemberitahuan/permohonan tertulis ke support@ayopajak.com.\n" +
								" \n" +
								"Perubahan Pernyataan Privasi\n" +
								"Pernyataan Privasi ini dapat diubah setiap saat dari waktu ke waktu dan akan berlaku pada tanggal yang ditentukan oleh kami. Setiap perubahan atas Pernyataan Privasi ini akan dipublikasikan di situs atau media lainnya yang dianggap perlu oleh kami. Mohon baca dan telusuri Pernyataan Privasi di situs secara berkala untuk melihat perubahan-perubahan tersebut. Dengan terus menggunakan setiap produk dan/atau Layanan kami atau situs kami setelah publikasi tersebut atau pemberitahuan dari kami, Anda dianggap setuju terhadap perubahan terhadap Pernyataan Privasi ini.\n" +
								" \n" +
								"Hukum dan Yurisdiksi Yang Berlaku\n" +
								"Pernyataan Privasi ini diatur oleh dan untuk ditafsirkan berdasarkan hukum Negara Republik Indonesia sesuai dengan Syarat & Ketentuan Umum Layanan GBU. Setiap dan seluruh sengketa yang timbul dari Pernyataan Privasi ini akan dirujuk ke dan pada akhirnya diselesaikan sesuai dengan Syarat & Ketentuan Umum Layanan GBU.\n" +
								" \n" +
								"Pengakuan dan Persetujuan\n" +
								"Dengan menggunakan setiap produk dan/atau Layanan kami atau situs kami, Anda menyatakan bahwa Anda telah membaca, memahami dan setuju terhadap ketentuan-ketentuan Pernyataan Privasi ini.",
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
							modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
							text = "Dengan ini saya membaca, memahami, dan menyetujui hal-hal yang tercantum pada Kebijakan Privasi yang berlaku",
							fontSize = 12.sp,
							fontWeight = FontWeight.Bold,
						)
					}

					Button(
						modifier = Modifier.height(48.dp).fillMaxWidth().padding(horizontal = 16.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							navigator.push(EmailVerification(email, true))
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