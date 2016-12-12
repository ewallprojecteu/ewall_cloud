package eu.ewall.platform.commons.datamodel.profile;

public class RewardCosts {
	
	public static final int REWARD_COST_WALLPAPER_ZERO_LEAVES = 2000;
	public static final int REWARD_COST_WALLPAPER_ONE_SIXTIES = 2500;
	public static final int REWARD_COST_WALLPAPER_TWO_STRIPES = 2500;
	public static final int REWARD_COST_WALLPAPER_THREE_BLUE = 1250;
	public static final int REWARD_COST_WALLPAPER_FOUR_YELLOW = 1250;
	
	public static int getRewardCosts(RewardType rewardType) {
		switch(rewardType) {
			
			case WALLPAPER_ZERO_LEAVES:
				return REWARD_COST_WALLPAPER_ZERO_LEAVES;
			
			case WALLPAPER_ONE_SIXTIES:
				return REWARD_COST_WALLPAPER_ONE_SIXTIES;
			
			case WALLPAPER_TWO_STRIPES:
				return REWARD_COST_WALLPAPER_TWO_STRIPES;
			
			case WALLPAPER_THREE_BLUE:
				return REWARD_COST_WALLPAPER_THREE_BLUE;
			
			case WALLPAPER_FOUR_YELLOW:
				return REWARD_COST_WALLPAPER_FOUR_YELLOW;
			
			default: return -1;
		}
		
	}
}
