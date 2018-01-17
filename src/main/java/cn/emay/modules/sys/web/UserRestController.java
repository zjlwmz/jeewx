package cn.emay.modules.sys.web;

import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;

import cn.emay.framework.core.beanvalidator.BeanValidators;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.service.UserService;

/**
 * User的Restful API的Controller.
 * 
 * @author liuht
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Controller
@RequestMapping(value = "/user")
public class UserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private Validator validator;

	/**
	 * 访问地址：http://localhost:8080/jeecg/rest/user
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<User> list() {
		List<User> listUsers=userService.getList(User.class);
		return listUsers;
	}

	/**
	 * 访问地址：http://localhost:8080/jeecg/rest/user/{id}
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		User task = userService.get(User.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody User user, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<User>> failures = validator.validate(user);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存用户
		userService.save(user);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = user.getId();
		URI uri = uriBuilder.path("/rest/user/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody User user) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<User>> failures = validator.validate(user);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		userService.saveOrUpdate(user);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		userService.deleteEntityById(User.class, id);
	}
}
