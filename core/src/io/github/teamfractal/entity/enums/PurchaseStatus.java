package io.github.teamfractal.entity.enums;

/**
 * The possible purchase status from market.
 */

// Extended by Josh added a FailPlayerNotEnoughResource so that when a player is selling to another player we can
// return this if the selling player does not have enough of the given resource
public enum PurchaseStatus {
	Success,
	FailMarketNotEnoughResource,
	FailPlayerNotEnoughMoney,
	FailPlayerNotEnoughResource
}
