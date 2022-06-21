package dev.road.map.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.road.map.commons.ParseUser;
import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.User;
import dev.road.map.domain.user.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
    
	@Autowired
    PasswordEncoder passwordEncoder;
    
	@Autowired
	ParseUser parseUser;

	@Autowired
	FileService fileService;
	
	@Value("${root}")
	private String root;

	@Value("${directory}")
	private String directory;

	@Value("${memberImagePath}")
	String memberImagePath;

	// 서비스를 이용한 유저 저장(user, email이 제대로 입력되었는지, 기존에 가입된 email인지 체크)
	public User create(final User user) {
		if (user == null || user.getEmail() == null) { // 입력된 user가 없는 경우 
			throw new RuntimeException("Invalid arguments");
		}
		final String email = user.getEmail();
		if (userRepository.findByEmail(email) != null) { // 이미 해당 이메일로 가입이 되어 있는 경우 
			throw new RuntimeException("Email already exist");
		}
		return userRepository.save(user);
	}
	
	// 입력받은 email로 유저 찾기-> matches메서드로 입력받은 password와 암호화된 password가 같은지 확인 
	public User getByCredential(final String email, final String password, final PasswordEncoder passwordEncoder) {
		final User originalUser = userRepository.findByEmail(email);
		
		//  BCryptPasswordEncoderd의 matchs메서드를 이용해 패스워드가 같은지 확인
		if (originalUser != null &&
				passwordEncoder.matches(password, originalUser.getPassword())) {
			return originalUser;
			
		}
		return null;
	}
	
	// 회원 정보 수정 
	@SuppressWarnings("unlikely-arg-type")
	public User edit(HttpServletRequest request, MultipartFile profile) {
    	String email = parseUser.parseEmail(request);
    	// 현재 로그인한 유저 
    	User user = userRepository.findByEmail(email);

    	String nickname = request.getParameter("nickname").trim();
		String password = request.getParameter("password").trim();
		String fieldStr = request.getParameter("field").trim();

		// 전체 저장경로 + 파일 이름
		// ex. ../gmail/hello.jpg
		String savedProfile = null;
		
		if (!profile.isEmpty()) {
			
			// 파일 업로드
			try {
				//	hello@naver.com
				String[] chunkEmail = email.split("@");
				String userEmail = chunkEmail[0]; // hello
				String[] chunkEmail2 = chunkEmail[1].split("."); 
				String userProvider = chunkEmail2[0]; // naver
				String savePath = "/" + directory + "/" + memberImagePath + "/" + userProvider; // 저장경로 
	
				List<String> path = new ArrayList<String>();
				path.add(root);
				path.add(directory);
				path.add(memberImagePath);
				path.add(userProvider);
	
				// 폴더 생성
				fileService.mkDir(path);

				// 원래 파일 명에서 확장자(.) 추출
				String ext = profile.getOriginalFilename().substring(profile.getOriginalFilename().indexOf("."));

				// 파일내용 + 파일명 --> 서버의 특정폴더(c:upload)에 영구저장. 서버가 종료되더라도 ec2 폴더에 저장.
				String newname = userEmail + ext;
				savedProfile = savePath + "/" + newname;
				
				user.setProfile(savedProfile);

				// 파일 업로드
				File serverfile = new File(root.concat(savePath + "/"), newname);
				serverfile.createNewFile();
				serverfile.setReadable(true, false);
				serverfile.setWritable(true, false);
				serverfile.setExecutable(true, false);
				profile.transferTo(serverfile);
				
				// DB에 업데이트 
				user.setProfile(savedProfile);
			} catch (Exception e) {
					e.printStackTrace();
				}
		} // if end
		
		// 수정 정보가 있을 때만 반영 
		if (!nickname.isEmpty()) {
			user.setNickname(nickname);
		}
		if (!password.isEmpty()) {
			user.setPassword(password);
		}
		if (!fieldStr.isEmpty()) {
			Field field;
			if ("back".equals(fieldStr)) {
				field = Field.back;
			}
			else {
				field = Field.front;
			}
			user.setField(field);
		}
		// 변경된 유저 정보 DB 저장
		userRepository.save(user);
		return user;
	}
	
	// 회원 탈퇴 
	public Boolean withdraw(HttpServletRequest request) {
    	String email = parseUser.parseEmail(request);
    	// 현재 로그인한 유저 
    	User user = userRepository.findByEmail(email);
    	user.setIsdelete(true);
		// 변경된 유저 정보 저장
    	userRepository.save(user);
		return true;
	}
}
