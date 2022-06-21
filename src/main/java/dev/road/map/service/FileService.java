package dev.road.map.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {

	@Value("${root}")
	String root;
	@Value("${route}")
	String route;

	// 폴더 생성
	public String mkDir(List<String> paths) {
		String completePath = "";
		for (String path : paths) {
			File dirPath = new File(completePath += path + route);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			} else {
				dirPath.setReadable(true, false);
				dirPath.setWritable(true, false);
				dirPath.setExecutable(true, false);
			}
		}
		return completePath;
	}
}