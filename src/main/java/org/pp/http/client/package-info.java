/**
 * http协议规范、报文结构 request response、header、entity(可否重复消费消息体？)
 * 默认情况下，HttpClient假定只有非实体封闭的方法（如GET和HEAD）是幂等的，幂等安全自动重试（server处理成功但是客户端收不到响应）
 * 如果响应中不存在Keep-Alive头，则HttpClient假定连接可以无限期地保持活动状态。
 * 但是，通常使用的许多HTTP服务器被配置为在一段时间不活动之后丢弃持久连接，以节省系统资源，而通常不通知客户端。
 */
package org.pp.http.client;