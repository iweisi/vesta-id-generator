package com.robert.vesta.rest.netty;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.intf.IdService;

@Sharable
public class VestaRestNettyServerHandler extends ChannelHandlerAdapter {
	private static final String ID = "id";

	private static final String VERSION = "version";
	private static final String TYPE = "type";
	private static final String GENMETHOD = "genMethod";
	private static final String MACHINE = "machine";
	private static final String TIME = "time";
	private static final String SEQ = "seq";

	private static final String ACTION_GENID = "/genid";

	private static final String ACTION_EXPID = "/expid";

	private static final String ACTION_TRANSTIME = "/transtime";

	private static final String ACTION_MAKEID = "/makeid";

	private static final Log log = LogFactory
			.getLog(VestaRestNettyServerHandler.class);

	private IdService idService;

	public VestaRestNettyServerHandler() {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"spring/vesta-rest-main.xml");
		idService = (IdService) ac.getBean("idService");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (!(msg instanceof HttpRequest))
			return;

		HttpRequest req = (HttpRequest) msg;

		if (is100ContinueExpected(req)) {
			ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
		}

		URI uri = new URI(req.getUri());

		if (log.isDebugEnabled())
			log.debug("request uri==" + uri.getPath());

		long id = -1;
		long time = -1;
		long version = -1;
		long type = -1;
		long genmethod = -1;
		long machine = -1;
		long seq = -1;

		QueryStringDecoder decoderQuery = new QueryStringDecoder(req.getUri());
		Map<String, List<String>> uriAttributes = decoderQuery.parameters();
		for (Entry<String, List<String>> attr : uriAttributes.entrySet()) {
			for (String attrVal : attr.getValue()) {
				if (log.isDebugEnabled())
					log.debug("Request Parameter: " + attr.getKey() + '='
							+ attrVal);

				if (ID.equals(attr.getKey())) {
					id = Long.parseLong(attrVal);
				} else if (TIME.equals(attr.getKey())) {
					time = Long.parseLong(attrVal);
				} else if (VERSION.equals(attr.getKey())) {
					version = Long.parseLong(attrVal);
				} else if (TYPE.equals(attr.getKey())) {
					type = Long.parseLong(attrVal);
				} else if (GENMETHOD.equals(attr.getKey())) {
					genmethod = Long.parseLong(attrVal);
				} else if (MACHINE.equals(attr.getKey())) {
					machine = Long.parseLong(attrVal);
				} else if (SEQ.equals(attr.getKey())) {
					seq = Long.parseLong(attrVal);
				}
			}
		}

		StringBuffer sbContent = new StringBuffer();

		if (ACTION_GENID.equals(uri.getPath())) {
			long idl = idService.genId();

			if (log.isTraceEnabled())
				log.trace("Generated id: " + idl);

			sbContent.append(idl);
		} else if (ACTION_EXPID.equals(uri.getPath())) {
			Id ido = idService.expId(id);

			if (log.isTraceEnabled())
				log.trace("Explained id: " + ido);

			JSONObject jo = JSONObject.fromObject(ido);

			sbContent.append(jo);
		} else if (ACTION_TRANSTIME.equals(uri.getPath())) {
			Date date = idService.transTime(time);

			if (log.isTraceEnabled())
				log.trace("Time: " + date);

			sbContent.append(date);
		} else if (ACTION_MAKEID.equals(uri.getPath())) {
			long madeId = -1;

			if (time == -1 || seq == -1)
				sbContent.append("Both time and seq are required.");
			else

			if (version == -1) {
				if (type == -1) {
					if (genmethod == -1) {
						if (machine == -1) {
							madeId = idService.makeId(time, seq);
						} else {
							madeId = idService.makeId(machine, time, seq);
						}
					} else {
						madeId = idService
								.makeId(genmethod, machine, time, seq);
					}
				} else {
					madeId = idService.makeId(type, genmethod, machine, time,
							seq);
				}
			} else {
				madeId = idService.makeId(version, type, genmethod, machine,
						time, seq);
			}


			if (log.isTraceEnabled())
				log.trace("Id: " + madeId);

			sbContent.append(madeId);

		} else {
			sbContent.append("\r\n");
			sbContent.append("Please input right URI:");
			sbContent.append("\r\n");
			sbContent.append("    Example 1: http://ip:port/genid");
			sbContent.append("\r\n");
			sbContent.append("    Example 2: http://ip:port/expid?id=?");
			sbContent.append("\r\n");
			sbContent.append("    Example 3: http://ip:port/transtime?time=?");
			sbContent.append("\r\n");
			sbContent.append("    Example 4: http://ip:port/makeid?version=?&type=?&genmethod=?&machine=?&time=?&seq=?");

		}

		if (log.isTraceEnabled())
			log.trace("Message body: " + sbContent);

		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
				Unpooled.wrappedBuffer(sbContent.toString().getBytes(
						Charset.forName("UTF-8"))));

		response.headers().set(CONTENT_TYPE, "text/plain");
		response.headers().set(CONTENT_LENGTH,
				response.content().readableBytes());

		boolean keepAlive = isKeepAlive(req);

		if (log.isTraceEnabled())
			log.trace("Keep Alive: " + keepAlive);

		if (!keepAlive) {
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		} else {
			response.headers().set(CONNECTION, Values.KEEP_ALIVE);
			ctx.write(response);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		if (log.isErrorEnabled())
			log.error("HTTP Server Error: ", cause);
		ctx.close();
	}
}
